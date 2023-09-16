
-- user dummy data 생성
insert into batch_study.users(`name`, `age`, `phone`, `email`) values ('최형우', 39, '010-****-****', 'best1@test.com');
insert into batch_study.users(`name`, `age`, `phone`, `email`) values ('김도영', 19, '010-****-****', 'best2@test.com');
insert into batch_study.users(`name`, `age`, `phone`, `email`) values ('나성범', 33, '010-****-****', 'best3@test.com');
insert into batch_study.users(`name`, `age`, `phone`, `email`) values ('양현종', 35, '010-****-****', 'best4@test.com');
insert into batch_study.users(`name`, `age`, `phone`, `email`) values ('윤영철', 19, '010-****-****', 'best5@test.com')

select * from batch_study.users;
select * from batch_study.send_coupon;