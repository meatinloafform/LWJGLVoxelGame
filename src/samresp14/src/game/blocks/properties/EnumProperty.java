package samresp14.src.game.blocks.properties;

public class EnumProperty<T extends Enum<T>> extends Property {
	public T value;
	
	public EnumProperty(T value) {
		this.value = value;
	}
	
	@Override
	public Property copy() {
		// TODO Auto-generated method stub
		return new EnumProperty<T>(value);
	}
}
