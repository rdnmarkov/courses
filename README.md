**Установка PostgreSQL**                                                                                                                                                                                                          
sudo apt update                                                                                                                                                                                                           
sudo apt install -y postgresql                                                                                                                                                                                                                 
Запуск и проверка статуса:                                                                                                                                                                                                       
sudo systemctl start postgresql                                                                                                                                                                                                  
sudo systemctl enable postgresql                                                                                                                                                                                                 
sudo systemctl status postgresql

**Настройка PostgreSQL**

sudo -u postgres psql

В psql выполнить:

CREATE USER postgres WITH PASSWORD 'postgres';
или
ALTER USER postgres WITH PASSWORD 'postgres';
если уже создан
CREATE DATABASE courses OWNER postgres;
GRANT ALL PRIVILEGES ON DATABASE courses TO postgres;

**Установка java**

sudo apt update
sudo apt install -y openjdk-21-jdk


**Сборка JAR**

На машине разработки:
mvn clean package -DskipTests
# JAR будет в target/courses-0.0.1-SNAPSHOT.jar

Скопируйте JAR на VDI:
root/bot

**Key Store**

Скопировать или создать файл /root/bot/keystore.p12

**Systemd сервис**

Создайте файл /etc/systemd/system/javabot.service:

	[Unit]
	Description=My Java Application
	After=syslog.target network.target

	[Service]
	User=root
	# Рабочая директория (где находится JAR)
	WorkingDirectory=/root/bot

	# Путь к Java и аргументы JVM
	Environment="BOT_ADMIN_CHANNEL=-123"
	Environment="BOT_KEY=123"
	Environment="BOT_NAME=J_bot:"
	Environment="DB_URL=jdbc:postgresql://localhost/db"
	Environment="DB_USER=admin"
	Environment="DB_PASSWORD=admin"
	Environment="SUB_CHANNEL=-1234"
	Environment="WEB_URL=https://10.10.10.10:8443/"
	Environment="KEY_STORE=/root/bot/keystore.p12"
	Environment="KEY_STORE_PASSWORD=1234"
	Environment="SERVER_PORT=8443"
	Environment="SSL_ENABLED=true"
	Environment="SHOW_SQL=false"


	# Команда запуска
	ExecStart=java -jar courses-0.0.1-SNAPSHOT.jar

	# Логирование (опционально)
	StandardOutput=journal
	StandardError=journal

	[Install]
	WantedBy=multi-user.target

**Запуск сервиса**

sudo systemctl daemon-reload
sudo systemctl enable javabot
sudo systemctl start javabot
sudo systemctl status javabot
sudo systemctl stop javabot
sudo systemctl restart javabot

**Просмотр логов**

sudo journalctl -u javabot -f