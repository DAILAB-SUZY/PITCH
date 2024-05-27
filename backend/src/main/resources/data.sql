DROP TYPE IF EXISTS rt;
create type rt as (f1 int, f2 text);

DO
'
DECLARE
    a1 rt[] := array[(1,''POP''), (2, ''ROCK''), (3, ''HIPHOP''), (4, ''COUNTRY''), (5, ''CLASSIC''), (6, ''JAZZ''), (7, ''ELECTRONIC''), (8, ''RNB''), (9, ''ROCK_METAL'')];
    f1 int;
    f2 text;
    f3 rt;
BEGIN
    FOREACH f3 in array a1 loop
            INSERT INTO genre (id, name) VALUES (f3.f1, f3.f2) ON CONFLICT DO NOTHING;
    end loop;
END;
';