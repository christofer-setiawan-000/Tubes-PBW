--users
CREATE TABLE users (
    username VARCHAR(30) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

--memes
CREATE TABLE memes (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    caption VARCHAR(500),
    image_url TEXT NOT NULL,
    username VARCHAR(30) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
    vote_count INT DEFAULT 0,
    comments_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--categories
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

--meme categories(many to many with categories) 

CREATE TABLE meme_categories (
    meme_id BIGINT NOT NULL REFERENCES memes(id) ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    PRIMARY KEY (meme_id, category_id)
);

--tasg
CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

--meme tags (many to may with tag)
CREATE TABLE meme_tags (
    meme_id BIGINT NOT NULL REFERENCES memes(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (meme_id, tag_id)
);

--comments
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    meme_id BIGINT NOT NULL REFERENCES memes(id) ON DELETE CASCADE,
    username VARCHAR(30) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
    content VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--votes
CREATE TABLE votes (
    id BIGSERIAL PRIMARY KEY,
    meme_id BIGINT NOT NULL REFERENCES memes(id) ON DELETE CASCADE,
    username VARCHAR(30) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
    voted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(meme_id, username)  -- user can only give one vote per meme
);

-- users -> memes	    1:M	Setiap user dapat mengunggah banyak meme
-- users -> comments	1:M	User dapat memberi banyak komentar
-- users -> votes       1:M	User dapat memberi banyak vote
-- memes -> comments    1:M	Satu meme punya banyak komentar
-- memes -> votes	    1:M	Satu meme punya banyak vote
-- memes <-> categories	M:N	Melalui meme_category
-- memes <-> tags	    M:N	Melalui meme_tag