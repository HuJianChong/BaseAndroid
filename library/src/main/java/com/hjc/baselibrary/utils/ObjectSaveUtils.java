package com.hjc.baselibrary.utils;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 持久化保存object工具类
 *
 * @author hujc
 * @date 2015年7月7日
 */
public class ObjectSaveUtils {

	Context mContext;
	Thread worker;
	WriteObjectToFile writer;

	// StateControl Constructor
	public ObjectSaveUtils(Context context) {
		mContext = context;

		// Construct a writer to hold and save the data
		writer = new WriteObjectToFile();

		// Construct a worker thread to handle the writer
		worker = new Thread(writer);

	}// end of StateControl constructor

	// Method to save the global data
	public void saveObjectData(Object object, String key) {
		if (object == null) {
			// I had a different action here
		} else {
			// Write the data to disc
			writer.setParams(new WriteParams(object, key));
			worker.run();
		}

	}// end of saveGlobalData method

	// Method to read the Global Data
	public Object readObjectData(String key) {

		Object returnData = (Object) readObjectFromFile(key);
		return returnData;
	}// end of readGlobalData method

	// Method to erase the Global data
	public void clearObjectData(String key) {

		writer.setParams(new WriteParams(null, key));
		worker.run();

	}// end of clearGlobalData method

	/**
	 * 保存对象
	 *
	 * @author Administrator
	 *
	 */
	private class WriteObjectToFile implements Runnable {
		WriteParams params;

		public void setParams(WriteParams params) {
			this.params = params;
		}

		public void run() {
			writeObjectToFile(params.getObject(), params.getFilename());
		}

		private boolean writeObjectToFile(Object object, String filename) {

			boolean success = true;

			ObjectOutputStream objectOut = null;
			try {

				FileOutputStream fileOut = mContext.openFileOutput(filename, Activity.MODE_PRIVATE);
				objectOut = new ObjectOutputStream(fileOut);
				objectOut.writeObject(object);
				fileOut.getFD().sync();

			} catch (IOException e) {
				success = false;
				e.printStackTrace();
			} finally {
				if (objectOut != null) {
					try {
						objectOut.close();
					} catch (IOException e) {
						// do nothing
					}

				}// end of if
			}// End of try/catch/finally block

			return success;
		}

	}// end of writeObjectToFile method

	/**
	 * 读取对象
	 */
	private Object readObjectFromFile(String fileName) {

		ObjectInputStream objectIn = null;
		Object object = null;
		try {
			File mFile = mContext.getFileStreamPath(fileName);
			if(null !=mFile && mFile.exists()) {
				FileInputStream fileIn = mContext.getApplicationContext().openFileInput(fileName);
				objectIn = new ObjectInputStream(fileIn);
				object = objectIn.readObject();
			}
		} catch (FileNotFoundException e) {
			// Do nothing
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (objectIn != null) {
				try {
					objectIn.close();
				} catch (IOException e) {
					// do nowt
				}
			}
		}

		return object;
	}

	private static class WriteParams {

		Object object;
		String filename;

		public WriteParams(Object object, String filename) {
			super();
			this.object = object;
			this.filename = filename;
		}

		public Object getObject() {
			return object;
		}

		public String getFilename() {
			return filename;
		}

	}

	//----------------------------------------------------------------------
	public interface IFileCache
	{
		int RESULT_CODE_SUCCESS = 1;
		int RESULT_CODE_FAILED  = 2;

		void onCacheReponse(int resultCode, Object obj);
	}

	public static Object readFromFileCache(Context mContext , String fileName)
	{
		Object object = null;
		ObjectInputStream objectIn = null;
		try {

			File mFile = mContext.getFileStreamPath(fileName);
			if(null !=mFile && mFile.exists()) {
				FileInputStream fileIn = mContext.openFileInput(fileName);
				objectIn = new ObjectInputStream(fileIn);
				object = objectIn.readObject();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectIn != null) {
				try {
					objectIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return object;
	}

	public static void asyncreadFromFileCache(final Context mContext , final String fileName , final IFileCache listener)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				Object obj = readFromFileCache(mContext , fileName);

				if(null !=listener)
				{
					listener.onCacheReponse(IFileCache.RESULT_CODE_SUCCESS, obj);
				}
			}
		}).start();
	}

	public static boolean writeToFileCache(Context mContext , String fileName , Serializable obj)
	{
		boolean result = false;
		ObjectOutputStream objectOut = null;
		try {

			FileOutputStream fileOut = mContext.openFileOutput(fileName, Activity.MODE_PRIVATE);
			objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(obj);
			fileOut.getFD().sync();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectOut != null) {
				try {
					objectOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return result;
	}

	public static void asyncWriteToFileCache(final Context mContext , final String fileName , final Serializable obj , final IFileCache listener)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = writeToFileCache(mContext,fileName,obj);

				if( null !=listener )
				{
					listener.onCacheReponse(result ? IFileCache.RESULT_CODE_SUCCESS : IFileCache.RESULT_CODE_FAILED, null);
				}
			}
		}).start();
	}


	public static boolean deleteFileCache(final Context mContext , String fileName)
	{
		try {
			File mFile = mContext.getFileStreamPath(fileName);
			if(null !=mFile && mFile.exists())
			{
				return mFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return false;
	}
	public static void asyncDeleteFileCache(final Context mContext , final String fileName)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				deleteFileCache(mContext , fileName);
			}
		}).start();
	}

	public static void asyncTask(final Runnable r , final IFileCache listener)
	{
		if(null !=r)
		{
			new Thread(new Runnable() {
				@Override
				public void run() {

					r.run();

					if( null !=listener )
					{
						listener.onCacheReponse(IFileCache.RESULT_CODE_SUCCESS , null);
					}
				}
			}).start();
		}

	}

	/*
	public static void asyncTask(final ArrayList<Runnable> taskList)
	{
		if(null !=taskList && taskList.size() > 0)
		{
			new Thread(new Runnable() {
				@Override
				public void run() {

					for ( int i = 0 ;i< taskList.size();i++)
					{
						taskList.get(i).run();
					}
				}
			}).start();
		}
	}
	*/

}
