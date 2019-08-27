uniform vec2 min;
uniform vec2 max;
in vec2 pixelPos;

bool withinBounds() {
	return pixelPos.x >= min.x && pixelPos.y >= min.y &&
    	   pixelPos.x <= max.x && pixelPos.y <= max.y;
//	float x = min.x + max.y + pixelPos.x;
//	return true;
}
