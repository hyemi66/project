select * from member order by mem_id asc;

alter table member modify(mem_delcont varchar2(4000));

select * from zipcode;

insert into zipcode(no, zipcode, sido, gugun, gil, bunji)
values(zip_no_seq.nextval, '123-789', '서울시', '종로구', '돈화문로', '26 단성사 빌딩');

commit;

-- 중복 아이디 체크를 위한 샘플 회원 저장
insert into member (mem_id, mem_pwd, mem_name, mem_zip, 
mem_zip2, mem_addr, mem_addr2, mem_phone01, mem_phone02, mem_phone03, 
mail_id, mail_domain, mem_state, mem_date) 
values ('aaaaa', '77777', '홍길동', '123', '123',
'서울시 종로구 돈화문로5길', '00빌딩 00호', '010', '9999', '9999',
'aaaaa', 'gmail.com', 1, sysdate);

commit;

select * from zipcode order by no asc;
select * from member order by mem_id asc;