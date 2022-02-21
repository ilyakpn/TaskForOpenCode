-- password - 'admin'

set @cnt = select count(id) from users where email = 'admin@mail.ru';

insert into users(email, password, role, status)
select email, password, role, status
from
    (
        values ('admin@mail.ru', '$2a$12$bL73OGR/aY.P8F92x2aLXeobrShtAaZJQvfPe0xzpwVz9WSHoS0ZK', 'ADMIN', 'ACTIVE')
    ) u(email, password, role, status)
where @cnt = 0;

