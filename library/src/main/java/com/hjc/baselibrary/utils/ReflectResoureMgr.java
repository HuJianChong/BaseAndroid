package com.hjc.baselibrary.utils;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * 反射得到资源ID 
 * @author long
 *
 */
public class ReflectResoureMgr {  
	
    private static final String TAG = ReflectResoureMgr.class.getName();
    private static ReflectResoureMgr instance;  
  
    private Class<?> CDrawable = null;
    private Class<?> CLayout = null;
    private Class<?> CId = null;
    private Class<?> CAnim = null;
    private Class<?> CStyle = null;
    private Class<?> CString = null;
    private Class<?> CArray = null;
  
    public static ReflectResoureMgr getInstance()
    {  
        if(instance == null)
        {  
            instance = new ReflectResoureMgr();  
        }  
        return instance;  
    }  
  
    public int getDrawableId(Context mContext, String resName){
        try{  
        	if(null==CDrawable)
        	{
        		CDrawable = Class.forName(mContext.getPackageName() + ".R$drawable");
        	}
        }catch(Exception e){
            Log.v(TAG,e.getMessage());
        }
        return getResId(mContext,CDrawable,resName);  
    }  
      
    public int getLayoutId(Context mContext, String resName){
        try{  
        	
        	if(null==CLayout)
        	{
        		CLayout = Class.forName(mContext.getPackageName() + ".R$layout");
        	}
        }catch(Exception e){
            Log.v(TAG,e.getMessage());
        }
        return getResId(mContext,CLayout,resName);  
    }  
      
    public int getIdId(Context mContext, String resName){
        try{  
        	if(null==CId)
        	{
        		 CId = Class.forName(mContext.getPackageName() + ".R$id");
        	}
        }catch(Exception e){
            Log.v(TAG,e.getMessage());
        } 
        return getResId(mContext,CId,resName);  
    }  
      
    public int getAnimId(Context mContext, String resName){
        try{  
        	
        	if(null==CAnim)
        	{
        		 CAnim = Class.forName(mContext.getPackageName() + ".R$anim");
        	}
        }catch(Exception e){
            Log.v(TAG,e.getMessage());
        }  
        return getResId(mContext,CAnim,resName);  
    }  
      
    public int getStyleId(Context mContext, String resName){
        try{  
        	
        	if(null==CStyle)
        	{
        		 CStyle = Class.forName(mContext.getPackageName() + ".R$style");
        	}
        }catch(Exception e){
            Log.v(TAG,e.getMessage());
        }  
        return getResId(mContext,CStyle,resName);  
    }  
      
    public int getStringId(Context mContext, String resName){
        try{  
        	if(null==CString)
        	{
        		CString = Class.forName(mContext.getPackageName() + ".R$string");
        	}
        }catch(Exception e){
            Log.v(TAG, e.getMessage());
        } 
        return getResId(mContext,CString,resName);  
    }  
      
    public int getArrayId(Context mContext, String resName){
        try{  
        	if(null==CArray)
        	{
        		CArray = Class.forName(mContext.getPackageName() + ".R$array");
        	}
        }catch(Exception e){
            Log.v(TAG,e.getMessage());
        } 
        return getResId(mContext,CArray,resName);  
    }  
      
    private int getResId(Context mContext, Class<?> resClass, String resName)
    {  
        if(resClass == null)
        {  
            Log.v(TAG,"getRes(null," + resName + ")");
            throw new IllegalArgumentException("ResClass is not initialized. Please make sure you have added neccessary resources. Also make sure you have " + mContext.getPackageName() + ".R$* configured in obfuscation. field=" + resName);
        }  
  
        try {  
            Field field = resClass.getField(resName);
            return field.getInt(resName);  
        } catch (Exception e) {
            Log.v(TAG, "getRes(" + resClass.getName() + ", " + resName + ")");
            Log.v(TAG, "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project. ");
            Log.v(TAG, e.getMessage());
        }   
        return -1;  
    }  
}  