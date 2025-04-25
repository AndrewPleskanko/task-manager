CREATE TABLE user_story_dependencies
(

    blocking_user_story_id BIGINT NOT NULL,

    blocked_user_story_id  BIGINT NOT NULL,

    PRIMARY KEY (blocking_user_story_id, blocked_user_story_id),

    FOREIGN KEY (blocking_user_story_id) REFERENCES user_stories (id),

    FOREIGN KEY (blocked_user_story_id) REFERENCES user_stories (id)

);