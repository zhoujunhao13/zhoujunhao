package com.zjh.test.groupby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class User {
	
	private Integer id;

    private String userName;

    private String password;

    private Integer age;
    
    private long c;

	public User() {
		super();
	}

	public User(Integer id, String userName, String password, Integer age) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.age = age;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public long getC() {
		return c;
	}

	public void setC(long c) {
		this.c = c;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", age=" + age + ", c=" + c
				+ "]";
	}
	
	
	public static void main(String[] args) {
		User u1 = new User(1, "aa", "aap", 23);
		User u2 = new User(2, "aa", "aap", 23);
		User u3 = new User(3, "bb", "aap", 23);
		User u4 = new User(4, "cc", "aap", 23);
		User u5 = new User(5, "cc", "aap", 23);
		User u6 = new User(6, "cc", "aap", 23);
		User u7 = new User(7, "aa", "aap", 24);
		List<User> list = new ArrayList<User>();
		list.add(u1);
		list.add(u2);
		list.add(u7);
		list.add(u3);
		list.add(u4);
		list.add(u5);
		list.add(u6);
		// 原有list(根据第二个字段:userName和第四个字段:age 统计重复的记录数)
		// jdk8的方法统计个数:
		Map<String, Map<Integer, Long>> map = list.stream().collect(
				Collectors.groupingBy(User::getUserName, Collectors.groupingBy(User::getAge, Collectors.counting())));
		// jdk8以下:
		// Map<String,Map<Integer,Long>> map=new HashMap<String,Map<Integer,Long>>();
		for (User user1 : list) {
			Map<Integer, Long> value = new HashMap<Integer, Long>();
			long count = 0;
			if (map.containsKey(user1.getUserName())) {
				continue;
			}
			for (int i = 0; i < list.size(); i++) {
				if (user1.getUserName().equals(list.get(i).getUserName()) && user1.getAge() == list.get(i).getAge()) {
					count += 1;
					value.put(user1.getAge(), count);
					map.put(user1.getUserName(), value);
				} else if (user1.getUserName().equals(list.get(i).getUserName())
						&& user1.getAge() != list.get(i).getAge()) {
					value.put(list.get(i).getAge(), Long.valueOf(1));
					map.put(user1.getUserName(), value);
				}
			}
		}
		map.forEach((k, v) -> {
			System.out.println(k + ">>>>" + v);
		});
		List<User> list2 = new ArrayList<User>();
		list.forEach(user -> {
			map.forEach((k, v) -> {
				if (k == user.getUserName()) {
					Long remove = v.remove(user.getAge());
					user.setC(null == remove ? 0 : remove);
				}
			});
			list2.add(user);
		});
		// 遍历最后想要的结果(User中c为统计后的个数，方便前台遍历集合时单元格合并行)
		list2.forEach(u -> {
			System.out.println(u);
		});
	}
}
