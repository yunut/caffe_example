USE caffe;

CREATE TABLE manager (
    `id` INT NOT NULL AUTO_INCREMENT,
    `phone_number` VARCHAR(13) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);