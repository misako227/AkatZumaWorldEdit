import json,re
result = {}
pattern = re.compile(r'this\.add\("([^"]+)"\,\s*"([^"]+)"\)')
with open(r"src\main\java\com\z227\AkatZumaWorldEdit\utilities\LanguageDataGenerator.java", "r", encoding="utf-8") as file:
  for line in file:
    match = pattern.search(line)
    if match:
        key = match.group(1)
        value = match.group(2)
        # print(key,value)
        result[key] = value

# print(result)

path = r'src\main\resources\assets\akatzumaworldedit\lang\zh_cn.json'
with open(path, "w", encoding="utf-8") as f:
  f.write(json.dumps(result, indent=2, ensure_ascii=False))

