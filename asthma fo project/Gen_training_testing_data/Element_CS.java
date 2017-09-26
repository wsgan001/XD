/********************************************************
*	Element_CS ���O					*
*							*
*	�w�q�]�����ت��C�Ӥ��������e			*
*	�ݭn�P�]�����ت�(ElementaryTable_CS)�f�t	*
*							*
*	�@�̡GLan, Guo-Cheng 	�̫�ק��:2008/12/25	*
********************************************************/

package Gen_training_testing_data;


class Element_CS{
	final char[] key;
	String value;
	int hash;
	Element_CS next;
	
	Element_CS(int h, char[] k, String v, Element_CS n) { 
    		hash = h;
        	key = k;
        	value = v;
        	next = n;
	}

	public char[] getKey() { 
        	return key; 
	}
	

	public String getInfo(){
		return value;
	}


	public boolean equals(Element_CS e) {  //�ˬdkey�ȬO�_�ۦP
		char[] ch = e.getKey();
		return equals(ch);
	}
	
	public boolean equals(char[] ch) {
		if (this.key.length!=ch.length) return false;
		for(int i=0; i<key.length; i++)
			if (key[i]!=ch[i]) return false;
		return true;
	}
}