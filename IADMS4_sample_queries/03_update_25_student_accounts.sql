USE iadms;

UPDATE STUDENT
SET Section = '2H-G2'
WHERE Person_ID BETWEEN 1 AND 25;

UPDATE ACCOUNT
SET Email = REPLACE(Email, '@school.edu', '@iadms.edu'),
    Password = 'updated1234'
WHERE Person_ID BETWEEN 1 AND 25;

SELECT *
FROM PERSON
JOIN STUDENT USING (Person_ID)
JOIN ACCOUNT USING (Person_ID)
ORDER BY Person_ID;
