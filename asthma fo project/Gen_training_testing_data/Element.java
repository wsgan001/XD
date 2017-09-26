/********************************************************
*	Element 類別					*
*							*
*	定義因素項目表中每個元素的內容			*
*	需要與因素項目表(ElementaryTable)搭配		*
*	元素內容為 key(char[]), 與 value(int)		*
*							*
*	作者：Bear	最後修改日:94/4/4		*
********************************************************/

package Gen_training_testing_data;

class Element {
	final char[] key;	//元素的key值
	int value;		//元素的value值
	int hash;		//元素的hash值
	Element next;		//相同hash值的下一個元素
	
	Element(int h, char[] k, int v, Element n) { 
    		hash = h;
        	key = k;
        	value = v;
        	next = n;
	}

	public char[] getKey() { 
        	return key; 
	}

	public int getValue() {
        	return value;
	}

	public boolean equals(Element e) {  //檢查key值是否相同
		char[] ch = e.getKey();
		return equals(ch);
	}
	
	public boolean equals(char[] ch) {
		if (this.key.length!=ch.length) return false;
		for(int i=0; i<key.length; i++)
			if (key[i]!=ch[i]) return false;
		return true;
	}
	/*
	public recordRemoval(HashMap m) {
		
		
        }*/
}