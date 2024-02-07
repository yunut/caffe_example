shell:
	docker-compose run --service-ports app bash

shell-mysql:
	docker-compose exec database-mysql mysql -uroot -proot

build: clean
	docker-compose build

up:
	docker-compose up

down:
	docker-compose down --remove-orphans
	rm -rf ./db/mysql/data