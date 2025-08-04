package com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape;

import com.z227.AkatZumaWorldEdit.Render.renderLine.RenderCurveLineBox;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineBase {
    // 1:对角线 2:曲线
    private int type;
    private List<BlockPos> posList;  // 控制点位
    boolean rightFlag;      //右键状态，true为右键选中了一个点位
    int rightCheckedIndex;       //右键选中点位的索引
    BlockPos rightPos;      //右键选中点位的坐标
    int MAX_POINT_NUM = 50; // 最大点位数
    int max_step = 10;      // 最大步进数
//    int max_interpolation = 1;     // 最大插值次数
    public List<BlockPos> curvePosList;  // 根据控制点位生成的曲线点位
    List<BlockPos> innerCurvePosList;   // 内部曲线点位
    int minPosY;

    public LineBase()
    {
        this.type = 1;
        this.posList = new ArrayList<>();
        this.rightFlag = false;
        this.rightCheckedIndex = 0;
        this.curvePosList = new ArrayList<>();
        this.innerCurvePosList = new ArrayList<>();
    }

    //服务端
    public LineBase(int type, List<BlockPos> posList){
//        this();
        this.type = type;
        this.posList = posList;
//        this.curvePosList = new ArrayList<>();


        //更新最小点位Y值，postList.size() > 2
        minPosY = posList.get(0).getY();
        for (BlockPos value : posList) {
            if (value.getY() < minPosY) {
                minPosY = value.getY();
            }
        }

        this.innerCurvePosList = new ArrayList<>();
        this.curvePosList = generateBezierCurve(max_step);


//        this.curvePosList.addAll(posList);

//        this.curvePosList.addAll(this.posList);
    }

    public void setRightPos(BlockPos pos){
        if(rightFlag){
            //修改点位
            if(!posList.contains(pos)){
                posList.set(rightCheckedIndex,pos);
                this.rightPos = pos;
                //更新最小点位Y值
                if(pos.getY() <= minPosY){
                    minPosY = pos.getY();
                }else{
                    minPosY = pos.getY();
                    for (BlockPos value : posList) {
                        if (value.getY() < minPosY) {
                            minPosY = value.getY();
                        }
                    }
                }
                this.curvePosList = generateBezierCurve(max_step);
                RenderCurveLineBox.updateVertexBuffer();
                return;
            }else{
                //如果选中的还是原点位，则取消选中
                if(pos.equals(rightPos)){
                    rightFlag = false;
                    this.rightPos = null;
                    RenderCurveLineBox.updateVertexBuffer();
                    return;
                }
                //如果选中的是其他点位，则修改选中点位
//                posList.set(rightCheckedIndex,pos);
                this.rightPos = pos;
                this.rightCheckedIndex = this.posList.indexOf(pos);
            }



        }else{
            if(this.posList.contains(pos)){
                this.rightFlag = true;
                this.rightCheckedIndex = this.posList.indexOf(pos);
                this.rightPos = pos;
            }
        }
        RenderCurveLineBox.updateVertexBuffer();
    }

    public void delPos()
    {
        if(rightFlag && rightPos !=null){
            BlockPos pos =  posList.remove(rightCheckedIndex);
            this.rightFlag = false;
            this.rightPos = null;

            //更新最小点位Y值
            if(pos.getY() <= minPosY && posList.size() > 1){
                for (BlockPos value : posList) {
                    if (value.getY() < minPosY) {
                        minPosY = value.getY();
                    }
                }
            }

            this.curvePosList = generateBezierCurve(max_step);
//            updateCurvePosList(pos);
        }
        RenderCurveLineBox.updateVertexBuffer();
    }

    public void delAllPos(){
        this.curvePosList.clear();
        this.posList.clear();
        this.rightFlag = false;
        this.rightCheckedIndex = 0;
        this.rightPos = null;
        RenderCurveLineBox.updateVertexBuffer();

    }


    public void addPosToStart(BlockPos pos)
    {
        if(!this.posList.contains(pos) && posList.size() < MAX_POINT_NUM){
            this.posList.add(0,pos);
            updateCurvePosList(pos);

        }
    }

    public void addPosToEnd(BlockPos pos)
    {
        if(!this.posList.contains(pos) && posList.size() < MAX_POINT_NUM){
            this.posList.add(pos);
            updateCurvePosList(pos);

        }

    }

    public void updateCurvePosList(BlockPos pos)
    {
        updateMinPos(pos);
        this.curvePosList = generateBezierCurve(max_step);
        RenderCurveLineBox.updateVertexBuffer();
    }

    public void updateMinPos(BlockPos pos)
    {
        if(posList.size() <= 1){
            minPosY = pos.getY();
            return;
        }
        if(pos.getY() < minPosY){
            minPosY = pos.getY();
            return;
        }
    }





    //线性插值
    public List<BlockPos> getCurvePointsLinear(List<BlockPos> curveList) {
        if(curveList.size() < 3) return curveList;
        List<BlockPos> curvePoints = new ArrayList<>();



        for (int i = 0; i < curveList.size() - 1; i++) {
            BlockPos controlPoint1 = curveList.get(i);
            BlockPos controlPoint2 = curveList.get(i + 1);

            double distance = calcDistance(controlPoint1, controlPoint2);
//            distance = distance * 2;
//            int numSteps = (int) (distance);
            for (int j = 0; j <= distance; j++) {
                double t = j / distance;
                double x = (1 - t) * controlPoint1.getX() + t * controlPoint2.getX();
                double y = (1 - t) * controlPoint1.getY() + t * controlPoint2.getY();
                double z = (1 - t) * controlPoint1.getZ() + t * controlPoint2.getZ();

                curvePoints.add(new BlockPos((int) x, (int) y, (int) z));
            }
        }

        return curvePoints;
    }


    //二次插值
    public List<BlockPos> getCurvePointsQuadratic(List<BlockPos> curveList) {
        if(curveList.size() < 3) return curveList;
        List<BlockPos> curvePoints = new ArrayList<>();


        for(int i = 0; i < curveList.size() - 3; i++){

            BlockPos controlPoint1 = curveList.get(i);
            BlockPos controlPoint2 = curveList.get(i + 1);
            BlockPos controlPoint3 = curveList.get(i + 2);


            double numSteps = calcDistance(controlPoint1, controlPoint2);
            double distance2 = calcDistance(controlPoint2, controlPoint3);
//            double maxDIstance = Math.max(distance1, distance2);
            if(numSteps < 1) return curveList;

//            if(maxDIstance >=10){
//                numSteps += maxDIstance / 5;
//            }
//            numSteps = (int) distance1;

            for (int j = 0; j <= numSteps; j++) {
                double t = (double) j / numSteps;
                double a = (1 - t) * (1 - t);
                double b = 2 * t * (1 - t);
                double c = t * t;

                double x = a * controlPoint1.getX() + b * controlPoint2.getX() + c * controlPoint3.getX();
                double y = a * controlPoint1.getY() + b * controlPoint2.getY() + c * controlPoint3.getY();
                double z = a * controlPoint1.getZ() + b * controlPoint2.getZ() + c * controlPoint3.getZ();

                curvePoints.add(new BlockPos((int) x, (int) y, (int) z));
            }
        }

        return curvePoints;
    }


    //三次插值
    public List<BlockPos> getCurvePointsCubic(List<BlockPos> curveList, int numSteps) {
        if(curveList.size() < 4) return curveList;
        List<BlockPos> curvePoints = new ArrayList<>();

        for (int i = 0; i < curveList.size() - 3; i++) {
            BlockPos controlPoint1 = curveList.get(i);
            BlockPos controlPoint2 = curveList.get(i + 1);
            BlockPos controlPoint3 = curveList.get(i + 2);
            BlockPos controlPoint4 = curveList.get(i + 3);

            for (int j = 0; j <= numSteps; j++) {
                double t = (double) j / numSteps;
                double a = (1 - t) * (1 - t) * (1 - t);
                double b = 3 * t * (1 - t) * (1 - t);
                double c = 3 * t * t * (1 - t);
                double d = t * t * t;

                double x = a * controlPoint1.getX() + b * controlPoint2.getX() + c * controlPoint3.getX() + d * controlPoint4.getX();
                double y = a * controlPoint1.getY() + b * controlPoint2.getY() + c * controlPoint3.getY() + d * controlPoint4.getY();
                double z = a * controlPoint1.getZ() + b * controlPoint2.getZ() + c * controlPoint3.getZ() + d * controlPoint4.getZ();

                curvePoints.add(new BlockPos((int) x, (int) y, (int) z));
            }
        }


        return curvePoints;
    }


    /**
     * 根据给定的控制点和步数，生成三维贝塞尔曲线上的点。
     *
     * @param numSteps      指定在0到1区间内划分的步数，决定了生成的曲线点的密度。
     * @return 包含曲线点的列表。
     */
    public List<BlockPos> generateBezierCurve(int numSteps) {
        int size = posList.size();
        if(size < 2) return posList;

        List<BlockPos> curvePoints = new ArrayList<>();
        List<BlockPos> innPoints = new ArrayList<>();
        int nextPosIndex = 1;
        double lastDistance = -1;
        this.innerCurvePosList.clear();


        int max_distance = (int) calculateMaxDistance(posList) ;
        numSteps = Math.max(numSteps, max_distance);

        double tIncrement = 1.0 / numSteps;  // 计算每一步的t值增量

        // 遍历所有t值，从0到1
        for (double t = 0.0; t <= 1.0; t += tIncrement) {
            double x = 0, y = 0, z = 0;
            int n = this.posList.size() -1;  // 获取控制点的数量减一

            // 遍历所有控制点，计算当前t值对应的曲线点
            for (int i = 0; i <= n; i++) {
                double b = binomialCoefficient(n, i) * Math.pow(1 - t, n - i) * Math.pow(t, i);  // 计算Bernstein基函数的值
                BlockPos controlPoint = this.posList.get(i);  // 获取当前控制点
                x += b * controlPoint.getX();  // 计算x坐标
                y += b * controlPoint.getY();  // 计算y坐标
                z += b * controlPoint.getZ();  // 计算z坐标



            }
//            double minY = curvePoints.stream().mapToDouble(BlockPos::getY).min().orElse(y);
            y = Math.max(y, minPosY);
            BlockPos currentPos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
            curvePoints.add(currentPos);  // 将计算出的点添加到结果列表中



        }

        for (int i = 0; i < curvePoints.size(); i += 4) { // 每次增加4来实现隔3个取1
            this.innerCurvePosList.add(curvePoints.get(i));
        }
        this.innerCurvePosList.add(curvePoints.get(curvePoints.size()-1));



        // 二次插值
//        curvePoints = getCurvePointsQuadratic(curvePoints,max_interpolation);
//        curvePoints = getCurvePointsCubic(curvePoints,1);
        //线性插值
        curvePoints = getCurvePointsLinear(curvePoints);

        Set<BlockPos> set = new HashSet<>(curvePoints);
        return  new ArrayList<>(set);

    }

    /**
     * 计算二项式系数。
     *
     * @param n 组合的总数。
     * @param k 选择的数量。
     * @return 返回二项式系数C(n,k)。
     */
    private static long binomialCoefficient(int n, int k) {
        long result = 1;
        if (k > n - k) {
            k = n - k;  // 优化计算过程，减少乘法次数
        }
        for (int i = 0; i < k; ++i) {
            result *= (n - i);  // 分子部分
            result /= (i + 1);  // 分母部分
        }
        return result;
    }



    public double calculateMaxDistance(List<BlockPos> posList){
        double maxDistance = 0;

        for (int i = 0; i < posList.size() - 1; i++) {
            BlockPos c1 = posList.get(i);
            BlockPos c2 = posList.get(i + 1);
            double distance = calcDistance(c1, c2);
            if (distance > maxDistance) {
              maxDistance = distance;
            }
        }

        return maxDistance;
    }





    public int getRightCheckedIndex() {
        return rightCheckedIndex;
    }

    public void setRightCheckedIndex(int rightCheckedIndex) {
        this.rightCheckedIndex = rightCheckedIndex;
    }

    public BlockPos getRightPos() {
        return rightPos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<BlockPos> getPosList() {
        return posList;
    }

    public void setPosList(List<BlockPos> posList) {
        this.posList = posList;
    }



    public void removePos(BlockPos pos)
    {
        this.posList.remove(pos);
    }

    public boolean isRightFlag() {
        return rightFlag;
    }

    public void setRightFlag(boolean rightFlag) {
        this.rightFlag = rightFlag;
    }

    public List<BlockPos> getCurvePosList() {
        return curvePosList;
    }

    public void setCurvePosList(List<BlockPos> curvePosList) {
        this.curvePosList = curvePosList;
    }

    public List<BlockPos> getInnerCurvePosList() {
        return innerCurvePosList;
    }

    public void setInnerCurvePosList(List<BlockPos> innerCurvePosList) {
        this.innerCurvePosList = innerCurvePosList;
    }

    public static double calcDistance(BlockPos pos1, BlockPos pos2){
        int x = Math.abs(pos1.getX() - pos2.getX()) + 1;
        int y = Math.abs(pos1.getY() - pos2.getY()) + 1;
        int z = Math.abs(pos1.getZ() - pos2.getZ()) + 1;
        return  Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }
}
