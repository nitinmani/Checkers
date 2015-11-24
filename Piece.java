import java.lang.Math;

public class Piece{
	private static String PAWN   = "pawn";
	private static String BOMB   = "bomb";
	private static String SHIELD = "shield";
	
	private boolean fire;
	private Board board;  // The board in which this piece is on
	private int x, y;  // x,y position of the piece
	private String type;
	
	private boolean crowned = false;
	private boolean hasKilled = false;
	
	private void setCrowned(){
		this.crowned = true;
	}
	
	public Piece(boolean isFire, Board b, int x, int y, String type){
		this.fire = isFire;
		this.board = b;
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public boolean isFire(){
		return this.fire;
	}
	
	public int side(){
		if (fire){
			return 0;
		}
		else{
			return 1;
		}
	}
	
	public boolean isKing(){
		if (this.crowned){
			return true;
		} else{
			return false;
		}
	}
	
	public boolean isBomb(){
		if (this.type.equalsIgnoreCase(Piece.BOMB)){
			return true;
		}
		return false;
	}
	
	public boolean isShield(){
		if (this.type.equalsIgnoreCase(Piece.SHIELD)){
			return true;
		}
		return false;
	}
	
	// TODO:  Implement this
	/**
	 * Assume this Piece's movement from its current position to (x,y) is valid.
	 * Moves the Piece to (x,y), capturing any intermediate piece, if applicable.
	 * 
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		if ((Math.abs(this.x - x) == 2) && board.pieceAt(((this.x + x) / 2), ((this.y + y) / 2)) != null) {
			this.hasKilled = true;
			int midX = this.x + x;
			int midY = this.y + y;
			if (this.isBomb()) {
				for (int i = x - 1; i <= x + 1; i++) {
					for (int j = y - 1; j <= y + 1; j++) {
						if (board.pieceAt(i, j) != null) {
							if (!board.pieceAt(i, j).isShield()) {
								board.remove(i,j);
							} 
						}
					}
				}
				if (board.pieceAt(midX, midY) != null && board.pieceAt(midX, midY).isShield()) {
					board.remove(((this.x + x) / 2), ((this.y + y) / 2));
				}
				board.remove(this.x, this.y);
				return;
			} else board.remove(((this.x + x) / 2), ((this.y + y) / 2));
		}
		board.place(board.remove(this.x, this.y), x, y);
		this.x = x;
		this.y = y;
		if (!isFire() && this.y == 0 || isFire() && this.y == 7) {
			this.setCrowned();
		}
	}
	
	// TODO:  Implement this
	/**
	 * Returns whether or not this Piece has captured another piece this turn.
	 * @return
	 */
	public boolean hasCaptured(){
		return this.hasKilled;
	}
	
	// TODO: Implement this
	/**
	 * Called at the end of each turn on the Piece that moved.  
	 * Makes sure the piece's hasCaptured() value returns to false.
	 */
	public void doneCapturing(){		
		if(this.hasCaptured()){
			this.hasKilled = false;
		}
	}
}