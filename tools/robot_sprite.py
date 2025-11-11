from PIL import Image, ImageDraw

def draw_robot(draw, facing):
    x_offset = 0
    body_color = (120, 180, 220, 255)
    eye_color = (255, 255, 255, 255)
    accent_color = (250, 220, 100, 255)
    shadow_color = (80, 130, 170, 255)

    # body
    draw.rectangle([x_offset + 8, 10, x_offset + 23, 25], fill=body_color, outline=shadow_color)

    # head
    draw.rectangle([x_offset + 10, 4, x_offset + 21, 12], fill=body_color, outline=shadow_color)

    # antenna
    draw.line([x_offset + 15, 2, x_offset + 15, 0], fill=accent_color, width=1)
    draw.ellipse([x_offset + 14, -1, x_offset + 16, 1], fill=accent_color)

    # eyes or bolts
    if facing == "front":
        draw.rectangle([x_offset + 12, 6, x_offset + 13, 7], fill=eye_color)
        draw.rectangle([x_offset + 18, 6, x_offset + 19, 7], fill=eye_color)
    elif facing == "right":
        draw.rectangle([x_offset + 17, 6, x_offset + 18, 7], fill=eye_color)
    elif facing == "back":
        draw.rectangle([x_offset + 12, 6, x_offset + 13, 7], fill=shadow_color)
        draw.rectangle([x_offset + 18, 6, x_offset + 19, 7], fill=shadow_color)

    # arms
    if facing == "front":
        draw.rectangle([x_offset + 5, 12, x_offset + 7, 18], fill=shadow_color)
        draw.rectangle([x_offset + 24, 12, x_offset + 26, 18], fill=shadow_color)
    elif facing == "right":
        draw.rectangle([x_offset + 25, 12, x_offset + 27, 18], fill=shadow_color)
    elif facing == "back":
        draw.rectangle([x_offset + 5, 12, x_offset + 7, 18], fill=shadow_color)
        draw.rectangle([x_offset + 24, 12, x_offset + 26, 18], fill=shadow_color)

    # legs
    draw.rectangle([x_offset + 12, 25, x_offset + 14, 30], fill=shadow_color)
    draw.rectangle([x_offset + 17, 25, x_offset + 19, 30], fill=shadow_color)

# Draw front, side, and back frames
directions = ["front", "right", "back"]
for i, facing in enumerate(directions):
    sprite_sheet = Image.new("RGBA", (32, 32), (0, 0, 0, 0))
    draw = ImageDraw.Draw(sprite_sheet)
    draw_robot(draw, facing)
    sprite_sheet.save("../assets/textures/robot_"+facing+".png")

