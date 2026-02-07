
CREATE TABLE  user (
                                             id INT NOT NULL AUTO_INCREMENT,
                                             name VARCHAR(45) NOT NULL,
                                             user_name VARCHAR(45) NOT NULL,
                                             password VARCHAR(45) NOT NULL,
                                             role VARCHAR(45) NOT NULL,
                                             PRIMARY KEY (id),
                                             UNIQUE INDEX `id_UNIQUE` (id ASC) VISIBLE,
                                             UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) VISIBLE);



CREATE TABLE  sentiment (
                                                  `id` INT NOT NULL,
                                                  `type` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                                  PRIMARY KEY (`id`),
                                                  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);



CREATE TABLE  category (
                                                 id INT NOT NULL,
                                                 name VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL,
                                                 PRIMARY KEY (`id`),
                                                 UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                                 UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);



CREATE TABLE  news (
                                             id INT  NOT NULL,
                                             headline TINYTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                             article_url TINYTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                             source VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                             published_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             thumbnail TINYTEXT NOT NULL,
                                             category_id INT NOT NULL,
                                             sentiment_id INT NOT NULL,
                                             PRIMARY KEY (`id`),
                                             UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                             UNIQUE INDEX `category_id_UNIQUE` (`category_id` ASC) VISIBLE,
                                             UNIQUE INDEX `sentiment_id_UNIQUE` (`sentiment_id` ASC) VISIBLE,
                                             CONSTRAINT `fk_news_on_sentiment`
                                                 FOREIGN KEY (`sentiment_id`)
                                                     REFERENCES sentiment (`id`)
                                                     ON DELETE CASCADE
                                                     ON UPDATE CASCADE,
                                             CONSTRAINT `fk_news_on_category`
                                                 FOREIGN KEY (`category_id`)
                                                     REFERENCES category (`id`)
                                                     ON DELETE  CASCADE
                                                     ON UPDATE  CASCADE);



CREATE TABLE  preferences (
                                                    id INT NOT NULL,
                                                    user_id INT NOT NULL,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                                    UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
                                                    CONSTRAINT `fk_preferences_on_user_id`
                                                        FOREIGN KEY (`user_id`)
                                                            REFERENCES user (`id`)
                                                            ON DELETE CASCADE
                                                            ON UPDATE CASCADE);




CREATE TABLE  preference_contents (
                                                            `id` INT NOT NULL,
                                                            `category_id` INT NOT NULL,
                                                            `sentiment_id` INT NOT NULL,
                                                            `preference_id` INT NOT NULL,
                                                            PRIMARY KEY (`id`),
                                                            UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                                            INDEX `category_id_idx` (`category_id` ASC) VISIBLE,
                                                            INDEX `sentiment_id_idx` (`sentiment_id` ASC) VISIBLE,
                                                            INDEX `preference_id_idx` (`preference_id` ASC) VISIBLE,
                                                            CONSTRAINT `fk_preference_contents_on_category`
                                                                FOREIGN KEY (`category_id`)
                                                                    REFERENCES `category` (`id`)
                                                                    ON DELETE CASCADE
                                                                    ON UPDATE CASCADE,
                                                            CONSTRAINT `fk_preference_contents_on_sentiment`
                                                                FOREIGN KEY (`sentiment_id`)
                                                                    REFERENCES `sentiment` (`id`)
                                                                    ON DELETE CASCADE
                                                                    ON UPDATE CASCADE,
                                                            CONSTRAINT `fk_preference_contents_on_preference`
                                                                FOREIGN KEY (`preference_id`)
                                                                    REFERENCES `preferences` (`id`)
                                                                    ON DELETE CASCADE
                                                                    ON UPDATE CASCADE);

