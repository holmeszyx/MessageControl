package z.hol.android.mc.utils;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;


public class Parser {
	
	private String mCommand;
	private String mInstruction = null;
	private String mInstructionMask = null;
	private String mTagStr = null;
	private String mQueryStr = null;
	private String[] mQuerys = null;
	private String[] mTags = null;
	
	public Parser (String command){
		mCommand = command; 
		parse();
	}
	
	private void parse(){
		int lastSplit = 0;
		int nextSplit = 0;
		int querySplit = -1;
		nextSplit = mCommand.indexOf('#', lastSplit);	// 1
		lastSplit = nextSplit + 1;
		nextSplit = mCommand.indexOf('#', lastSplit);	// 2
		if (nextSplit == -1){
			// 没有 '#'
			querySplit = mCommand.indexOf('?', lastSplit);
			if (querySplit == -1){
				// 没有 ‘#’和 '?'
				mInstruction = mCommand.substring(lastSplit, mCommand.length()).trim();
			}else{
				// 有 '?'
				mInstruction = mCommand.substring(lastSplit, querySplit).trim();
			}
		}else {
			// 有 '#',则有目标对象
			mInstruction = mCommand.substring(lastSplit, nextSplit).trim();
			lastSplit = nextSplit + 1;
			querySplit = mCommand.indexOf('?', lastSplit);
			if (querySplit == -1){
				// 没有 '?'
				mTagStr = mCommand.substring(lastSplit, mCommand.length()).trim();
			}else{
				// 有 '?'
				mTagStr = mCommand.substring(lastSplit, querySplit).trim();
				
			}
		}
		parseQuery(querySplit);
		parseInstruction();
		parseTags();
	}
	
	private void parseQuery(int querySplit){
		if (querySplit != -1){
			mQueryStr = mCommand.substring(querySplit).trim();
		}
		if (!TextUtils.isEmpty(mQueryStr)){
			mQuerys = mQueryStr.split(",");
		}
	}
	
	private void parseInstruction(){
		String[] inst = mInstruction.split("\\.");
		mInstruction = inst[0];
		if (inst.length > 1){
			mInstructionMask = inst[1];
		}
	}
	
	private void parseTags(){
		if (TextUtils.isEmpty(mTagStr)){
			return;
		}
		String[] tags = mTagStr.split(",");
		List<String> tagList = new ArrayList<String>();
		if (tags.length > 0){
			for (String tag : tags){
				if (Utils.isPhoneNumber(tag)){
					tagList.add(tag);
				}
			}
		}
		if (!Utils.isListEmpty(tagList)){
			tagList.toArray(mTags);
		}
		tagList.clear();
	}
	
	public String getInstruction(){
		return mInstruction;
	}
	
	public String getInstructionMask(){
		return mInstructionMask;
	}
	
	public String[] getTags (){
		return mTags;
	}
	
	public String[] getQuerys(){
		return mQuerys;
	}
	
	public boolean isAvaliable(){
		return !TextUtils.isEmpty(mInstruction);
	}
}
