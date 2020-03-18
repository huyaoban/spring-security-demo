create table `users` (
	`id` bigint(20) not null auto_increment,
	`username` varchar(50) not null,
	`password` varchar(60),
	`enable` tinyint(4) not null default 1 comment '用户是否可用',
	`roles` text comment '用户角色，多个角色之间用逗号隔开',
	primary key (`id`),
	key `username` (`username`)
);

INSERT INTO `spring-demo`.`users`(`id`, `username`, `password`, `enable`, `roles`) VALUES (1, 'user', '123', 1, 'ROLE_USER');
INSERT INTO `spring-demo`.`users`(`id`, `username`, `password`, `enable`, `roles`) VALUES (2, 'admin', '123', 1, 'ROLE_USER,ROLE_ADMIN');
