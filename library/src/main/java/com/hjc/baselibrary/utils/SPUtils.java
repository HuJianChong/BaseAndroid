package com.hjc.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * sp相关处理类
 */
public class SPUtils {

	private SPUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("SPUtils cannot be instantiated");
	}

	/**
	 * 保存在手机里面的文件名
	 */
	public static final String FILE_NAME = "share_data";

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 *
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void put(Context context, String key, Object object) {
		put(context, FILE_NAME, key, object);
	}

	public static void put(Context context, String fileName, String key, Object object) {
		if (null == context || null == key || null == object) {
			return ;
		}
		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}
		SharedPreferencesCompat.apply(editor);
	}

	public static void put(Context context, HashMap<String , Object> kvs) {
		put(context, FILE_NAME, kvs);
	}

	public static void put(Context context, String fileName, HashMap<String , Object> kvs)
	{
		if (null == context || null == kvs || kvs.size() <= 0) {
			return ;
		}

		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		Iterator iter = kvs.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
			String key = entry.getKey();
			Object object = entry.getValue();

			if (object instanceof String) {
				editor.putString(key, (String) object);
			} else if (object instanceof Integer) {
				editor.putInt(key, (Integer) object);
			} else if (object instanceof Boolean) {
				editor.putBoolean(key, (Boolean) object);
			} else if (object instanceof Float) {
				editor.putFloat(key, (Float) object);
			} else if (object instanceof Long) {
				editor.putLong(key, (Long) object);
			} else {
				editor.putString(key, object.toString());
			}
		}
		SharedPreferencesCompat.apply(editor);
	}

	public static void putSet(Context context , String key , Set<String> set)
	{
		put(context, FILE_NAME, key, set);
	}

	public static void putSet(Context context, String fileName , String key , Set<String> set)
	{
		StringBuilder sb = new StringBuilder();
		for (String str:set) {
			if(sb.length()>0)
			{
				sb.append(",");
			}
			sb.append(str);
		}

		put( context, fileName , key , sb.toString() );
	}

	public static Set<String> getSet(Context context, String fileName , String key)
	{
		String obj = (String)get(context, fileName, key, "");
		if(!TextUtils.isEmpty(obj))
		{
			String[]array = obj.split(",");
			if(array.length>0)
			{
				Set<String> ret = new HashSet<>(array.length);
				for (int i = 0 ;i < array.length;i++)
				{
					String item = array[i].trim();
					if(!TextUtils.isEmpty(item))
					{
						ret.add(item);
					}
				}
				return ret;
			}
		}
		return null;
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 *
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject) {
		return get(context,FILE_NAME,key,defaultObject);
	}

	public static Object get(Context context, String fileName , String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}


	public static HashMap get(Context context, String fileName , String[] keys, Class<?>[] types) {

		if(null == keys || null == types || (keys.length != types.length))
		{
			return null;
		}

		HashMap ret = new HashMap(keys.length);

		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		for (int i = 0; i < keys.length; i++) {
			String key  =  keys[i];
			String type =  types[i].getName();
			if("int".equals(type))
			{
				ret.put(key,sp.getInt(key, -1));
			}
			else if("long".equals(type))
			{
				ret.put(key,sp.getLong(key , -1L));
			}
			else if("float".equals(type))
			{
				ret.put(key,sp.getFloat(key,-1F));
			}
			else if("boolean".equals(type))
			{
				ret.put(key,sp.getBoolean(key,false));
			}
			else if("java.lang.String".equals(type))
			{
				ret.put(key,sp.getString(key,null));
			}
			else
			{
				//not found !
			}
		}

		return ret;
	}

	/**
	 * 移除某个key值已经对应的值
	 *
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 *
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	public static void clear(Context context , String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 *
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 *
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 *
	 * @author zhy
	 *
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 *
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 *
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}

}
