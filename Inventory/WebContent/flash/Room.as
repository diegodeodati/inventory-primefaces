package {
	public class Room {
		private var roomName:String;
		private var roomFloor:Number;
		private var roomPalace:String;
		
		function Room(roomName:String, roomFloor:Number, roomPalace:String) {
			this.roomName = roomName;
			this.roomFloor = roomFloor;
			this.roomPalace = roomPalace;
		}
		 
		public function GetNameAndPosition():String {
			//return "Nome: " + roomName + "\n" + "Piano: " + roomFloor + "\n" + "Palazzina: " + roomPalace;
			return roomName;
		}
	}
}