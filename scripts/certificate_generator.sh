#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

detect_ipv4() {
  # 1) Tillåt override via env
  if [[ -n "${BROKER_IP:-}" ]]; then
    echo "$BROKER_IP"; return
  fi

  # 2) Linux: iproute2
  if command -v ip >/dev/null 2>&1; then
    ip -4 route get 1.1.1.1 2>/dev/null \
      | awk '{for(i=1;i<=NF;i++){ if($i=="src"){print $(i+1); exit} }}' && return
  fi

  # 3) macOS
  if [[ "$OSTYPE" == darwin* ]]; then
    ipconfig getifaddr en0 2>/dev/null || ipconfig getifaddr en1 2>/dev/null && return
  fi

  # 4) Git Bash / Windows (PowerShell)
  if [[ "$OSTYPE" == msys || "$OSTYPE" == cygwin ]]; then
    if command -v powershell.exe >/dev/null 2>&1; then
      ip=$(powershell.exe -NoProfile -Command "(Get-NetIPAddress -AddressFamily IPv4 | Where-Object { \$_.IPAddress -notmatch '^169\.254\.' -and \$_.IPAddress -ne '127.0.0.1' } | Select-Object -First 1 -ExpandProperty IPAddress)" \
          | tr -d '\r')
      if [[ -n "$ip" ]]; then echo "$ip"; return; fi
    fi
    # Fallback: plocka första icke-loopback/icke-APIPA ur ipconfig
    ipconfig | grep -Eo '([0-9]{1,3}\.){3}[0-9]{1,3}' | grep -Ev '^127\.|^169\.254\.' | head -n1 && return
  fi

  # 5) Sista utvägen
  echo "127.0.0.1"
}

IP="$(detect_ipv4)"
echo "Using local IP: $IP"

CERT_LIFETIME=3650

# CERTIFICATE AUTHORITY VARIABLES
CA_DIR=../src/main/resources/authorization/ca
CA_CN="pluto_payments"
CA_FILE_NAME="ca"

# SERVER VARIABLES
GEN_SERVER=1 # if 1 will generate server certificates
SERVER_DIR=../src/main/resources/authorization/server
SERVER_FILE_NAME="server"
SERVER_CN="pluto_payments"

# CLIENT VARIABLES
GEN_CLIENT=1  # if 1 will generate client certificates
GEN_CLIENT_PEM=1 # if 1 will generate .pem files for client
CLIENT_DIR=../src/main/resources/authorization/client
CLIENT_NAME="admin"
CLIENT_PASSWORD="admin123"
CLIENT_FILE_NAME="client"
CLIENT_CN="client"

# KEYSTORE VARIABLES
KEYSTORE_DIR=../src/main/resources/authorization/keystore
KEYSTORE_ALIAS="admin"
KEYSTORE_PASS="admin123"

# TRUSTSTORE VARIABLES
TRUSTSTORE_ALIAS="admin"
TRUSTSTORE_PASS="admin123"
TRUSTSTORE_DIR=../src/main/resources/authorization/truststore

# CREATE DIRECTORIES
mkdir -p "$CA_DIR"
mkdir -p "$SERVER_DIR"
mkdir -p "$CLIENT_DIR"
mkdir -p "$KEYSTORE_DIR"
mkdir -p "$TRUSTSTORE_DIR"

# GENERATE CERTIFICATE AUTHORITY CERTIFICATE AND KEY
if [ -f "$CA_DIR/$CA_FILE_NAME.key" ] && [ -f "$CA_DIR/$CA_FILE_NAME.crt" ]; then
  echo "Certificate and key already created."
else
cat > "$CA_DIR/ca_req.cnf" <<EOF
[ req ]
prompt = no
distinguished_name = dn

[ dn ]
C  = SE
O  = Pluto
CN = $CA_CN
EOF

  echo "Creating new cert and key..."
  openssl genrsa -out "$CA_DIR/$CA_FILE_NAME.key" 2048
  openssl req -x509 -new -nodes -key "$CA_DIR/$CA_FILE_NAME.key" -sha256 -days $CERT_LIFETIME \
    -out "$CA_DIR/$CA_FILE_NAME.crt" -config "$CA_DIR/ca_req.cnf"
fi

# GENERATE SERVER CERTIFICATE AND KEY
if [ "$GEN_SERVER" = 1 ]; then
cat > "$SERVER_DIR/$SERVER_FILE_NAME-san.cnf" <<EOF
[ req ]
default_bits       = 2048
prompt             = no
default_md         = sha256
req_extensions     = req_ext
distinguished_name = dn

[ dn ]
CN = ${SERVER_CN}

[ req_ext ]
subjectAltName     = @alt_names
extendedKeyUsage   = serverAuth
keyUsage           = digitalSignature, keyEncipherment
basicConstraints   = CA:FALSE

[ alt_names ]
IP.1  = ${IP}
DNS.1 = localhost
EOF

  # CREATE CERTIFICATE AND KEY
  openssl genrsa -out "$SERVER_DIR/$SERVER_FILE_NAME.key" 2048
  openssl req -new -key "$SERVER_DIR/$SERVER_FILE_NAME.key" -out "$SERVER_DIR/$SERVER_FILE_NAME.csr" -config "$SERVER_DIR/$SERVER_FILE_NAME-san.cnf"
  openssl x509 -req -in "$SERVER_DIR/$SERVER_FILE_NAME.csr" -CA "$CA_DIR/$CA_FILE_NAME.crt" -CAkey "$CA_DIR/$CA_FILE_NAME.key" -CAcreateserial \
    -out "$SERVER_DIR/$SERVER_FILE_NAME.crt" -days $CERT_LIFETIME -sha256 -extensions req_ext -extfile "$SERVER_DIR/$SERVER_FILE_NAME-san.cnf"

  # PREPARE THEM FOR USE IN SPRING BOOT
  openssl pkcs12 -export -in "$SERVER_DIR/$SERVER_FILE_NAME.crt" -inkey "$SERVER_DIR/$SERVER_FILE_NAME.key" -certfile "$CA_DIR/$CA_FILE_NAME.crt" \
    -name "$KEYSTORE_ALIAS" -out "$KEYSTORE_DIR/keystore.p12" -passout pass:"$KEYSTORE_PASS"
  keytool -importcert -trustcacerts -file "$CA_DIR/$CA_FILE_NAME.crt" -alias "$TRUSTSTORE_ALIAS" -keystore "$TRUSTSTORE_DIR/truststore.p12" \
    -storetype PKCS12 -storepass "$TRUSTSTORE_PASS" -noprompt

  echo "$SERVER_FILE_NAME certificate created for IP: $IP"
fi

# GENERATE CLIENT CERTIFICATE AND KEY
if [ "$GEN_CLIENT" = 1 ]; then
cat > "$CLIENT_DIR/$CLIENT_FILE_NAME-san.cnf" <<EOF
[ req ]
default_bits       = 2048
prompt             = no
default_md         = sha256
req_extensions     = req_ext
distinguished_name = dn

[ dn ]
CN = ${CLIENT_NAME}

[ req_ext ]
extendedKeyUsage   = clientAuth
keyUsage           = digitalSignature, keyEncipherment
basicConstraints   = CA:FALSE
EOF

  mkdir -p "$CLIENT_DIR"

  openssl genrsa -out "$CLIENT_DIR/$CLIENT_FILE_NAME.key" 2048
  openssl req -new -key "$CLIENT_DIR/$CLIENT_FILE_NAME.key" -out "$CLIENT_DIR/$CLIENT_FILE_NAME.csr" -config "$CLIENT_DIR/$CLIENT_FILE_NAME-san.cnf"
  openssl x509 -req -in "$CLIENT_DIR/$CLIENT_FILE_NAME.csr" -CA "$CA_DIR/$CA_FILE_NAME.crt" -CAkey "$CA_DIR/$CA_FILE_NAME.key" -CAcreateserial \
    -out "$CLIENT_DIR/$CLIENT_FILE_NAME.crt" -days $CERT_LIFETIME -sha256 -extensions req_ext -extfile "$CLIENT_DIR/$CLIENT_FILE_NAME-san.cnf"

  echo "$CLIENT_FILE_NAME cert created for $CLIENT_NAME"

  # GENERATE PEM FILES IF NEEDED
  if [ "$GEN_CLIENT_PEM" = 1 ]; then
    cp "$CLIENT_DIR/$CLIENT_FILE_NAME.crt" "$CLIENT_DIR/${CLIENT_FILE_NAME}-cert.pem"
    cp "$CLIENT_DIR/$CLIENT_FILE_NAME.key" "$CLIENT_DIR/${CLIENT_FILE_NAME}-key.pem"
    cp "$CA_DIR/$CA_FILE_NAME.crt"         "$CA_DIR/${CA_FILE_NAME}-cert.pem"

    echo "$CLIENT_FILE_NAME pem files created for $CLIENT_NAME"
  fi
fi