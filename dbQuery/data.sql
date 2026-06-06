--mock data to test stuff 

INSERT INTO users (username, email, password, name, role) VALUES
('memeLord', 'memelord@example.com', 'pass_1', 'Alex Johnson', 'admin'),
('funnyGuy', 'funnyg@example.com', 'pass_2', 'Brian Lee', 'user'),
('catQueen', 'catqueen@example.com', 'pass_3', 'Samantha Ray', 'user'),
('coderMeme', 'codermeme@example.com', 'pass_4', 'Jordan Park', 'moderator'),
('gifWizard', 'gifwizard@example.com', 'pass_5', 'Leslie Kim', 'user');

INSERT INTO memes (title, caption, image_url, username, vote_count, comments_enabled) VALUES
('When You Fix a Bug', 'And it breaks everything else...', '/data/1765420543742_content_warning_05bcb97b.webm', 'coderMeme', 12, TRUE),
('Cat Vibing', 'Certified vibing moment', '/data/1765420589599_Screenshot_2025-11-11_142922.png', 'catQueen', 30, TRUE),
('Monday Mood', 'The face you make entering work on Monday', '/data/1765713787041_Beliau_Memanggil_Anda2.png', 'funnyGuy', 18, TRUE),
('This is Fine', 'Everything is burning but you smile', '/data/1765712969732_TTDPEDS.png', 'gifWizard', 22, TRUE);

INSERT INTO categories (name) VALUES
('Programming'),
('Animals'),
('Relatable'),
('Work'),
('Classic');

INSERT INTO meme_categories (meme_id, category_id) VALUES
(1, 1),
(1, 4),

(2, 2),
(2, 3),

(3, 3),
(3, 4),

(4, 1),
(4, 3),
(4, 5),

(5, 3),
(5, 5);

INSERT INTO tags (name) VALUES
('funny'),
('code'),
('cat'),
('monday'),
('chaos'),
('reaction'),
('classic');

INSERT INTO meme_tags (meme_id, tag_id) VALUES
(1, 1),
(1, 2),
(1, 6),

(2, 1),
(2, 3),

(3, 1),
(3, 4),

(4, 1),
(4, 2),
(4, 7),

(5, 1),
(5, 5),
(5, 6);

INSERT INTO comments (meme_id, username, content) VALUES
(13, 'funnyGuy', 'LOL this is so accurate!'),
(13, 'gifWizard', 'Story of my life.'),
(18, 'memeLord', 'Cat supremacy!'),
(19, 'catQueen', 'This hits too hard...'),
(20, 'funnyGuy', 'Happens every day.'),
(20, 'coderMeme', 'This is literally my workplace.');

INSERT INTO votes (meme_id, username) VALUES
(1, 'funnyGuy'),
(1, 'gifWizard'),
(2, 'memeLord'),
(2, 'funnyGuy'),
(2, 'coderMeme'),
(3, 'catQueen'),
(3, 'memeLord'),
(4, 'catQueen'),
(4, 'gifWizard'),
(5, 'memeLord'),
(5, 'funnyGuy');

