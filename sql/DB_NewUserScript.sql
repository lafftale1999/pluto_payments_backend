CREATE USER 'pluto_banker'@'localhost' IDENTIFIED BY 'pluto123';
GRANT ALL PRIVILEGES ON pluto.* TO 'pluto_banker'@'localhost';
FLUSH PRIVILEGES;