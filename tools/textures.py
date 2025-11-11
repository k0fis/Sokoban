from PIL import Image, ImageDraw

TILE_SIZE = 32

def make_wall():
    """Draws a simple shaded wall block."""
    img = Image.new("RGBA", (TILE_SIZE, TILE_SIZE), (100, 100, 100, 255))
    draw = ImageDraw.Draw(img)

    # Brick pattern
    for y in range(0, TILE_SIZE, 8):
        offset = (y // 8) % 2 * 4
        for x in range(-offset, TILE_SIZE, 8):
            draw.rectangle([x, y, x + 6, y + 4], fill=(120, 120, 120, 255))
    draw.rectangle([0, 0, TILE_SIZE - 1, TILE_SIZE - 1], outline=(60, 60, 60, 255))
    return img

def make_box():
    """Draws a cartoon wooden box."""
    img = Image.new("RGBA", (TILE_SIZE, TILE_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    draw.rectangle([2, 2, TILE_SIZE-3, TILE_SIZE-3], fill=(180, 120, 40, 255), outline=(90, 60, 20, 255))
    # wood planks
    for x in range(4, TILE_SIZE-3, 8):
        draw.line([x, 2, x, TILE_SIZE-3], fill=(150, 90, 30, 255), width=1)
    # inner frame
    draw.rectangle([6, 6, TILE_SIZE-7, TILE_SIZE-7], outline=(90, 60, 20, 255))
    draw.line([6, 6, TILE_SIZE-7, TILE_SIZE-7], fill=(90, 60, 20, 255))
    draw.line([6, TILE_SIZE-7, TILE_SIZE-7, 6], fill=(90, 60, 20, 255))
    return img

def make_destination():
    """Draws the destination (goal) tile."""
    img = Image.new("RGBA", (TILE_SIZE, TILE_SIZE), (210, 210, 210, 255))
    draw = ImageDraw.Draw(img)

    # subtle checker pattern
    for y in range(0, TILE_SIZE, 8):
        for x in range(0, TILE_SIZE, 8):
            if (x // 8 + y // 8) % 2 == 0:
                draw.rectangle([x, y, x + 8, y + 8], fill=(200, 200, 200, 255))
    draw.rectangle([0, 0, TILE_SIZE - 1, TILE_SIZE - 1], outline=(180, 180, 180, 255))

    # goal marker (target circle)
    draw.ellipse([8, 8, 24, 24], outline=(255, 80, 80, 255), width=3)
    draw.ellipse([12, 12, 20, 20], outline=(255, 150, 150, 255), width=2)
    return img

def make_floor():
    """Draws a basic walkable floor tile."""
    img = Image.new("RGBA", (TILE_SIZE, TILE_SIZE), (210, 210, 210, 255))
    draw = ImageDraw.Draw(img)

    # subtle checker pattern
    for y in range(0, TILE_SIZE, 8):
        for x in range(0, TILE_SIZE, 8):
            if (x // 8 + y // 8) % 2 == 0:
                draw.rectangle([x, y, x + 8, y + 8], fill=(200, 200, 200, 255))
    draw.rectangle([0, 0, TILE_SIZE - 1, TILE_SIZE - 1], outline=(180, 180, 180, 255))
    return img

# Generate all textures
wall = make_wall()
box = make_box()
destination = make_destination()
floor = make_floor()

# Save individual textures
wall.save("../assets/textures/wall.png")
box.save("../assets/textures/box.png")
destination.save("../assets/textures/goal.png")
floor.save("../assets/textures/floor.png")

# Create combined tileset (3 tiles wide)
#tileset = Image.new("RGBA", (TILE_SIZE * 4, TILE_SIZE), (0, 0, 0, 0))
#tileset.paste(wall, (0, 0))
#tileset.paste(box, (TILE_SIZE, 0))
#tileset.paste(destination, (TILE_SIZE * 2, 0))
#tileset.paste(floor, (TILE_SIZE * 3, 0))
#tileset.save("../assets/textures/tileset.png")

