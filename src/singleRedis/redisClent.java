package singleRedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class redisClent {
	@Test
	public void testSingleRedis() {
		// 创建一个Jedis的连接
		Jedis jedis = new Jedis("192.168.0.11", 6379);
		// 执行redis命令
		jedis.set("mytest", "hello world, this is jedis client!");
		// 从redis中取值
		String result = jedis.get("mytest");
		// 打印结果
		System.out.println(result);
		// 关闭连接
		jedis.close();
	}

	@Test
	public void testJedisPool() {
		// 创建一连接池对象
		JedisPool jedisPool = new JedisPool("192.168.0.11", 6379);
		// 从连接池中获得连接
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get("mytest");
		System.out.println(result);
		// 关闭连接
		jedis.close();

		// 关闭连接池
		jedisPool.close();
	}

	/**
	 * String 类型的操作
	 */
	@Test
	public void testRedisString() { // 设置键 获取键 get set
		Jedis jedis = new Jedis("192.168.0.11", 6379);
		jedis.set("myJavaString", "myJavaString");
		System.out.println("设置后值：" + jedis.get("myJavaString"));
		// 追加键 append
		jedis.append("myJavaString", "开始了");
		System.out.println("追加后值：" + jedis.get("myJavaString"));
		// 删除操作 del
		jedis.del("myJavaString");
		System.out.println("删除后值：" + jedis.get("myJavaString"));
		// 不存在就保存， setnx msetnx
		jedis.setnx("myJavaString", "myJavaString");
		System.out.println("设置后值：" + jedis.get("myJavaString"));
		System.out.println("再次设置后值：" + jedis.setnx("myJavaString", "myJavaString"));
		// 截取字符串 substr
		System.out.println("截取后值：" + jedis.substr("myJavaString", 0, 4));
		// 设置多个键值对 mset mget
		jedis.mset(new String[] { "zhangsan", "123", "lisi", "1234" });
		System.out.println("多次设置后值：" + jedis.mget("zhangsan", "lisi"));
		// 递增递减 incr decr incrby decrby
		// 123递增1之后为124,1234递减1后为1233
		jedis.incr("zhangsan");
		jedis.decr("lisi");
		System.out.println("递增递减后值：" + jedis.mget("zhangsan", "lisi"));
		// 124递增6后为130,1233递减3后为1233
		jedis.incrBy("zhangsan", 6);
		jedis.decrBy("lisi", 3);
		System.out.println("递增递减后值：" + jedis.mget("zhangsan", "lisi"));
	}

	/**
	 * 列表(List) 可重复 先进后出
	 */
	@Test
	public void testList() {
		Jedis jedis = new Jedis("192.168.0.11", 6379);
		// 尾添加 rpush 头添加 lpush
	/*	jedis.lpush("books", "java", "C++", "Ruby", "Scala", "python");
		jedis.rpush("language", "java", "C++", "Ruby", "Scala", "python");
		// -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
		System.out.println("头添加后books值:" + jedis.lrange("books", 0, -1));
		System.out.println("尾添加后language值:" + jedis.lrange("language", 0, -1));
		// 尾部删除 rpop 头部删除 lpop
		System.out.println("删除的值为：" + jedis.lpop("books"));
		System.out.println("删除的值为：" + jedis.rpop("language"));
		System.out.println("头部删除后books值:" + jedis.lrange("books", 0, -1));
		System.out.println("尾部删除后language值:" + jedis.lrange("language", 0, -1));*/
		// 尾部删除并头添加 rpoplpush
		jedis.rpoplpush("language", "books");
		System.out.println("尾部删除并头添加后books值:" + jedis.lrange("books", 0, -1));
		System.out.println("尾部删除并头添加后language值:" + jedis.lrange("language", 0, -1));
		// 区别: 只能给存在的list做添加，不能项lpush那样能新增list
		/*	jedis.lpushx("books", "php");
		jedis.lpushx("book", "php");
		System.out.println("头添加后books值:" + jedis.lrange("books", 0, -1));
		System.out.println("头添加后book值:" + jedis.lrange("book", 0, -1));
		// 获取集合长度 llen 指定索引的值 lindex 保留截取的值 ltrim
		System.out.println("books集合长度:" + jedis.llen("books"));
		System.out.println("books集合第二个数值:" + jedis.lindex("books", 1));
		jedis.ltrim("books", 0, 2);
		System.out.println("截取后books值:" + jedis.lrange("books", 0, -1));*/
	}

}
