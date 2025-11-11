from PIL import Image, ImageDraw, ImageFont

# ---------- CONFIG ----------
INPUT = "../assets/logo/logo-sq.png"
OUTPUT = "../assets/logo/logo-text.png"

TITLE = "SOKOBAN"
SUBTITLE = "pro Kubu"

TITLE_SIZE = 120
SUBTITLE_SIZE = 64

# Barvy

TEXT_COLOR = (155, 35, 22, 200)
TEXT_COLOR2 = (155, 35, 22, 30)
OUTLINE_COLOR = (0, 0, 0, 250)
OUTLINE_COLOR2 = (0, 0, 0, 50)
OUTLINE_WIDTH = 2

FONT_PATHS = [
    "../assets/fonts/PressStart2P.ttf"
]

# ---------- FONT LOADING ----------
def load_font(size):
    for p in FONT_PATHS:
        try:
            return ImageFont.truetype(p, size)
        except:
            pass
    return ImageFont.load_default()

font_title = load_font(TITLE_SIZE)
font_subtitle = load_font(SUBTITLE_SIZE)

# ---------- LOAD IMAGE ----------
base = Image.open(INPUT).convert("RGBA")
W, H = base.size

overlay = Image.new("RGBA", (W, H), (0, 0, 0, 0))
draw = ImageDraw.Draw(overlay)

# ---------- DRAW TEXT WITH OUTLINE + ALPHA ----------
def draw_text_with_outline(text, y, font, fill, outline, outline_width, xadd):
    # Bounding box: pro centrování
    bbox = draw.textbbox((0, 0), text, font=font)
    w = bbox[2] - bbox[0]
    h = bbox[3] - bbox[1]

    x = xadd + (W - w) / 2

    # Outline (8 směrů)
    for ox in range(-outline_width, outline_width + 1):
        for oy in range(-outline_width, outline_width + 1):
            if ox == 0 and oy == 0:
                continue
            draw.text((x + ox, y + oy), text, font=font, fill=outline)

    # Main text (s průhledností)
    draw.text((x, y), text, font=font, fill=fill)

# ---------- DRAW ----------

draw_text_with_outline("KFS", 80, font_subtitle, TEXT_COLOR2, OUTLINE_COLOR2, OUTLINE_WIDTH, 310)

pos = 750
draw_text_with_outline(TITLE, 200, font_title, TEXT_COLOR, OUTLINE_COLOR, OUTLINE_WIDTH, 0)
draw_text_with_outline(SUBTITLE, 900, font_subtitle, TEXT_COLOR, OUTLINE_COLOR, OUTLINE_WIDTH, 175)

# ---------- SAVE ----------
result = Image.alpha_composite(base, overlay)
result.save(OUTPUT, "PNG")

