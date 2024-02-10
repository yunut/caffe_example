USE caffe;

CREATE TABLE user (
    `id` VARCHAR(36) NOT NULL,
    `phone_number` VARCHAR(13) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `roles` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (phone_number)
);

CREATE TABLE product (
    `bar_code` VARCHAR(36) NOT NULL,
    `category` VARCHAR(255) NOT NULL,
    `sale_price` INT NOT NULL,
    `origin_price` INT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `expire_date` DATE NOT NULL,
    `size` VARCHAR(20) NOT NULL,
    `created_by` VARCHAR(36) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (bar_code)
)