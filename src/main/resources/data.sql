insert into Member values(90001, sysdate(), 'User1', 'test1111', '701010-1111111'); --'테이블 Member'에 더미데이터 넣기
insert into Member values(90002, sysdate(), 'User2', 'test2222', '801010-1111111');
insert into Member values(90003, sysdate(), 'User3', 'test3333', '901010-1111111');


insert post values(10001, 'My first post', 90001);
insert post values(10002, 'My second post', 90001);