
MERGE INTO ratings r
USING (SELECT 'G' AS name) AS vals
ON r.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO ratings r
USING (SELECT 'PG' AS name) AS vals
ON r.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO ratings r
USING (SELECT 'PG-13' AS name) AS vals
ON r.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO ratings r
USING (SELECT 'R' AS name) AS vals
ON r.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO ratings r
USING (SELECT 'NC-17' AS name) AS vals
ON r.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO genres g
USING (SELECT 'Комедия' AS name) AS vals
ON g.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO genres g
USING (SELECT 'Драма' AS name) AS vals
ON g.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO genres g
USING (SELECT 'Мультфильм' AS name) AS vals
ON g.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO genres g
USING (SELECT 'Триллер' AS name) AS vals
ON g.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);


MERGE INTO genres g
USING (SELECT 'Документальный' AS name) AS vals
ON g.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

MERGE INTO genres g
USING (SELECT 'Боевик' AS name) AS vals
ON g.name = vals.name
WHEN NOT MATCHED THEN
  INSERT(name) VALUES (vals.name);

