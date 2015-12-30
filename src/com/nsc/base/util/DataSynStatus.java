package com.nsc.base.util;

public enum DataSynStatus {
	ARCHIVE{//归档文件
		@Override
		public String toString() {
			return "L09";
		}
	}, 
	UPDATE{//更新文件,如果path不为空，为文件迁移
		@Override
		public String toString() {
			return "L04";
		}
	},  
	REMOVE{
		@Override
		public String toString() {
			return "L05";
		}
	},
	DELETE{//删除文件
		@Override
		public String toString() {
			return "L07";
		}
	},  
	DESTROY{//销毁文件
		@Override
		public String toString() {
			return "L03";
		}
		
	}, 
	RESTORE{//恢复文件
		@Override
		public String toString() {
			return "L08";
		}
	},
	INSERT{//插入
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "L06";
		}
	}
}
