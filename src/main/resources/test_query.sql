SELECT ip FROM logs.log where log_date between '2017-01-01.13:00:00' and '2017-01-01.14:00:00' group by ip having count(request) >= 100;

SELECT request FROM logs.log where ip = ?;

SELECT * FROM logs.blocked;