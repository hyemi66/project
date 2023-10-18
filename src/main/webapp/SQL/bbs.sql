alter table bbs modify (bbs_cont varchar2(4000));

select * from bbs order by bbs_no desc;

drop table bbs;
drop sequence bbs_no_seq;

-- bbs_no_seq ������ ����
create sequence bbs_no_seq
start with 1
increment by 1
nocache;

delete from bbs where bbs_no=3;
commit;
select * from bbs order by bbs_no desc;