CREATE DATABASE cloud_storage_db;
USE cloud_storage_db;
SHOW TABLES;
SELECT * FROM file_metadata;
DESCRIBE file_metadata;
ALTER TABLE file_metadata
ADD COLUMN s3_key VARCHAR(255);
SELECT * FROM file_metadata;
SELECT * FROM file_metadata;
SELECT * FROM file_metadata;
DESCRIBE file_metadata;
SELECT id, file_name, s3key, s3_key, file_url
FROM file_metadata;
ALTER TABLE file_metadata
DROP COLUMN s3_key;
SELECT * FROM file_metadata
