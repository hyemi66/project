select * from member order by mem_id asc;

alter table member modify(mem_delcont varchar2(4000));

select * from zipcode;

insert into zipcode(no, zipcode, sido, gugun, gil, bunji)
values(zip_no_seq.nextval, '123-789', '�����', '���α�', '��ȭ����', '26 �ܼ��� ����');

commit;

-- �ߺ� ���̵� üũ�� ���� ���� ȸ�� ����
insert into member (mem_id, mem_pwd, mem_name, mem_zip, 
mem_zip2, mem_addr, mem_addr2, mem_phone01, mem_phone02, mem_phone03, 
mail_id, mail_domain, mem_state, mem_date) 
values ('aaaaa', '77777', 'ȫ�浿', '123', '123',
'����� ���α� ��ȭ����5��', '00���� 00ȣ', '010', '9999', '9999',
'aaaaa', 'gmail.com', 1, sysdate);

commit;

select * from zipcode order by no asc;
select * from member order by mem_id asc;