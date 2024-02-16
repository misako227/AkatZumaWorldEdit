[toc]

<br>

[语雀文档地址](https://www.yuque.com/u39444834/dgakrb/gt2eg4whuq7hcvwf?singleDoc#)

作者：[5中生有](https://center.mcmod.cn/60332/) 、AkatZuma、Akashiya_yukina

MCMOD：https://www.mcmod.cn/class/13584.html

### 指令列表

|  指令   | 描述  |
|  ----  | ----  |
| /a set | 设置选区内方块 |
| /a stack      | 堆叠选区内方块 |
| /a copy      | 复制 |
| /a paste      | 粘帖 |
| /a flip      | 翻转复制的内容 |
| /a replace      | 替换 |
| /a line      | 连线 |
| /a undo      | 撤销 |
| /a redo      | 撤销undo |
| /a cyl        | 实心圆形/圆柱体 |
| /a hcyl        | 空心圆形/圆柱体 |
| /a sphere     | 球 |
| /a ellipse    | 椭圆 |

| 管理员指令      |  |
|  ----  | ----  |
| /a add viplayer | 添加高级玩家 |
| /a del viplayer | 删除高级玩家 |


### 说明
* 所有功能都只扣除背包，撤销不会返还，也不会产生掉落物
  <br>

- - -

### 快捷键
`ctrl + u` 撤销（undo）

### 工具
#### 选区工具
使用本MOD的选区工具，`左键`选取第一个点，`右键`选取第二个点。

`左/右键`空气选择玩家头部位置的点位
![wood.jpg](img/wood.jpg)

<br>

- - -
#### 查询工具
`左键`查看方块状态，消息可以点击复制状态

`右键`放置一个上次查询方块，需要背包中有对应的方块物品

<br>

- - -
#### 建筑耗材方块(猫猫虫)
![bc.jpg](img/bc.jpg)

复制任何方块都只消耗此材料,可带到头部

方块ID：`akatzumaworldedit:building_consumable`

● 注意：因为只消耗此MOD的物品，会产生复制方块的问题 

● `不想复制的方块`加入黑名单即可

● `不想使用复制功能`的把`此方块`加入黑名单

适用于指令`/a copy 、 /a stack`

<br>

- - -

#### 投影
![pro1.png](img/pro1.png)

![pro2.jpg](img/pro2.jpg)

<br>

- - -

### 指令
```java
//格式
/a 指令 <必填参数> [选填参数]
```

#### /a set 设置选区内方块
`/a set <方块ID>`
可以使用查询工具查看方块的状态并复制,[]内是方块的各种状态

比如：`/a set minecraft:cherry_leaves[persistent=true]`

放置一个不会枯萎的樱花树叶，默认放置的树叶是会枯萎的
<br>

- - -
#### /a stack 堆叠
默认根据玩家朝向东南西北堆叠，上下方向需要填入参数

`/a stack <堆叠次数> [方向]`
- <堆叠次数>： 数字即可，受选区范围限制
- [方向]：可选参数`up、down`

<br>

- - -

#### /a copy 复制
复制选区内的方块，使用`/a paste` 粘帖
> 复制时候玩家的站位会影响粘帖的位置和翻转，粘帖时候会根据玩家朝向来粘帖
* <font color='red'>注意</font>： 不会复制NBT属性，比如箱子里面的数据等，只复制方块状态

<br>

#### /a flip 翻转
`/a flip [up]`

* `[up]`（选填参数）上下翻转
* 向下翻转：玩家站在`选区`**下方**复制，填入参数`up`
* 向上翻转：玩家站在`选区`**上方**复制，填入参数`up`
> 位置影响参考复制↑

* 翻转复制的选区内容，不填参数默认左右翻转

* 以玩家复制时候的位置为原点翻转



<br>

#### /a paste 粘帖
`/a flip [-a]`
* 会根据玩家朝向来旋转复制的内容粘帖
* `-a` （选填参数）粘帖的时候忽略空气

<br>

- - -

#### /a replace 替换
`/a replace <被替换的方块> <替换成的方块>`
<br>
- - -

#### /a line 连线/对角线
`/a replace <方块ID>`

在选区两点之间连一条线

长度限制：大于5

高度限制：大于1
<br>
- - -

### 生成指令
#### /a cyl  实心圆形/圆柱体
`/a cyl <方块ID> <半径> <高度> [x角度] [z角度]`
* `<半径>` 最小3
* `<高度>` 最小1
* `[x角度]` （选填参数）沿x轴旋转生成的圆，范围360至-360
* `[z角度]` （选填参数）沿z轴旋转生成的圆，范围360至-360
<br>
- - -

#### /a hcyl  空心圆形/圆柱体
`/a hcyl <方块ID> <半径> <高度> [x角度] [z角度]`
* `<半径>` 最小3
* `<高度>` 最小1
* `[x角度]` （选填参数）沿x轴旋转生成的圆，范围360至-360
* `[z角度]` （选填参数）沿z轴旋转生成的圆，范围360至-360

![hcyl.jpg](img/hcyl.jpg)

  <br>
- - -

#### /a sphere  球
`/a sphere <方块ID> <半径> [-h]`
* `<半径>` 最小3
* `[-h]` （选填参数）生成空心球，不填默认生成实心的
<br>
- - -

#### /a ellipse  椭圆
`/a ellipse <方块ID> <东西半径> <南北半径> <高度> [-h]`
* `<东西半径>` 最小3，X轴
* `<南北半径>` 最小3，Z轴
* `<高度>`    最小3，Y轴
* `[-h]` （选填参数）生成空心椭圆，不填默认生成实心的

### 管理员指令
#### /a add viplayer 添加高级玩家 
`/a add viplayer <玩家名字>`

#### /a del viplayer 删除高级玩家 
`/a del viplayer <玩家名字>`


### Debug
debug日志格式

`[玩家名字][指令][玩家坐标][方块ID][选区坐标1][选区坐标2]`
```log
  [Dev][a set minecraft:birch_planks][BlockPos{x=-103, y=63, z=63}][Birch Planks=Block{minecraft:birch_planks}][BlockPos{x=-107, y=64, z=63}][BlockPos{x=-107, y=65, z=62}]
```


### 更新日志
v1.0.2
- 修复楼梯的翻转
- 添加圆的xz轴旋转
- 添加复制时候的投影
- 添加投影物品
- 添加`/a line`指令
- 添加粘帖时候忽略空气
- 添加快捷键`ctrl+u`撤销


v1.0.1
- 添加实心圆柱体`/a cyl`指令
- 添加空心圆柱体`/a hcyl`指令
- 添加球体`/a sphere`指令
- 添加椭圆`/a ellipse`指令
- 添加`/a pos1 /a pos2`指令
- `paste` `set` 修改为放置不更新相邻方块