USE caffe;

CREATE TABLE user (
    `id` INT NOT NULL AUTO_INCREMENT,
    `phone_number` VARCHAR(13) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);