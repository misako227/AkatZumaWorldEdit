import json

with open("zh.txt", "r", encoding="utf-8") as r:
  lines = r.read()

lines = lines.replace('this.add(','').replace(');',',').replace('", "', '": "')
lines = '{'+lines[:-1]+'}'
path = r'src\main\resources\assets\akatzumaworldedit\lang\zh_cn.json'
with open(path, "w", encoding="utf-8") as f:
  f.write(json.dumps(json.loads(lines), indent=2, ensure_ascii=False))
input("continue")