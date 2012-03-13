package net.idea.qmrf.client;



public enum QMRFRoles {
	
	qmrf_user {
		@Override
		public String toString() {
			return "Author";
		}
		@Override
		public String getHint() {
			return "Registered user and QMRF documents author";
		}		
	},
	qmrf_editor {
		//can publish new documents
		@Override
		public String getURI() {
			return Resources.editor;
		}
		@Override
		public String toString() {
			return "New document";
		}
		@Override
		public String getHint() {
			return "Publishing new documents";
		}
	},
	qmrf_manager {
		//can do sysadmin
		@Override
		public String getURI() {
			return Resources.admin;
		}	
		@Override
		public String toString() {
			return "Admin";
		}		
		@Override
		public String getHint() {
			return "System administration";
		}
	},
	qmrf_admin {
		//can review documents - used no more
		@Override
		public String toString() {
			return "Reviewer";
		}
		@Override
		public String getHint() {
			return "Not used anymore";
		}
	};
	public String getURI() {return null;}
	public String getHint() { return toString();}

}
