<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stat-cards">
      <!-- 总订单数 -->
      <div class="stat-card card-primary">
        <div class="stat-content">
          <div>
            <p class="stat-label">总订单数</p>
            <h3 class="stat-value">{{ formatNumber(statistics.totalOrder) }}</h3>
            <p class="stat-trend up">
              <el-icon><ArrowUp /></el-icon> 12.5% 较上周
            </p>
          </div>
          <div class="stat-icon">
            <el-icon><ShoppingBag /></el-icon>
          </div>
        </div>
      </div>

      <!-- 用户总数 -->
      <div class="stat-card card-success">
        <div class="stat-content">
          <div>
            <p class="stat-label">注册用户</p>
            <h3 class="stat-value">{{ formatNumber(statistics.totalUser) }}</h3>
            <p class="stat-trend up">
              <el-icon><ArrowUp /></el-icon> 5.2% 较上周
            </p>
          </div>
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
        </div>
      </div>

      <!-- 配送员数 -->
      <div class="stat-card card-warning">
        <div class="stat-content">
          <div>
            <p class="stat-label">配送员数</p>
            <h3 class="stat-value">{{ formatNumber(statistics.totalDelivery) }}</h3>
            <p class="stat-trend">
              3 人待审核
            </p>
          </div>
          <div class="stat-icon">
            <el-icon><Bicycle /></el-icon>
          </div>
        </div>
      </div>

      <!-- 通知公告 -->
      <div class="stat-card card-info">
        <div class="stat-content">
          <div>
            <p class="stat-label">通知公告</p>
            <h3 class="stat-value">{{ formatNumber(statistics.totalNotice) }}</h3>
            <p class="stat-trend">
              已发布
            </p>
          </div>
          <div class="stat-icon">
            <el-icon><Bell /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-row">
      <!-- 折线图 -->
      <div class="chart-card chart-large">
        <div class="chart-header">
          <h3>近 {{ trendDays }} 天订单趋势</h3>
          <el-select v-model="trendDays" size="small" @change="loadTrendData" class="trend-select">
            <el-option label="最近7天" :value="7" />
            <el-option label="最近30天" :value="30" />
          </el-select>
        </div>
        <div ref="lineChartRef" class="chart"></div>
      </div>

      <!-- 饼图 -->
      <div class="chart-card chart-small">
        <h3 class="chart-title">订单状态分布</h3>
        <div ref="pieChartRef" class="chart"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { getHomeStatistics, getOrderStatusStatistics, getOrderDailyStatistics } from '@/api/statistics'
import { ArrowUp, User, Bicycle, ShoppingBag, Bell } from '@element-plus/icons-vue'

const statistics = ref({
  totalOrder: 1286,
  totalUser: 892,
  totalDelivery: 56,
  totalNotice: 12
})
const trendDays = ref(7)
const lineChartRef = ref(null)
const pieChartRef = ref(null)
let lineChart = null
let pieChart = null

const formatNumber = (num) => {
  if (!num) return '0'
  return num.toLocaleString()
}

const initLineChart = (data) => {
  if (!lineChartRef.value) return
  lineChart = echarts.init(lineChartRef.value)

  const dates = data ? data.map(item => item.date?.substring(5) || item.date) : ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  const counts = data ? data.map(item => item.count) : [120, 132, 101, 134, 90, 230, 210]

  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e2e8f0',
      borderWidth: 1,
      textStyle: { color: '#1e293b' },
      padding: [12, 16],
      extraCssText: 'box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); border-radius: 8px;'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: { lineStyle: { color: '#e2e8f0' } },
      axisLabel: { color: '#64748b', fontSize: 12 }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisLabel: { color: '#64748b', fontSize: 12 },
      splitLine: { lineStyle: { color: '#f1f5f9', type: 'dashed' } }
    },
    series: [{
      name: '订单量',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      lineStyle: { color: '#4f46e5', width: 3 },
      itemStyle: { color: '#4f46e5', borderColor: '#fff', borderWidth: 2 },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(79, 70, 229, 0.3)' },
          { offset: 1, color: 'rgba(79, 70, 229, 0.05)' }
        ])
      },
      data: counts
    }]
  }
  lineChart.setOption(option)
}

const initPieChart = (data) => {
  if (!pieChartRef.value) return
  pieChart = echarts.init(pieChartRef.value)

  const statusMap = { 0: '待接单', 1: '配送中', 2: '已完成', 3: '已取消' }
  const colorMap = { 0: '#6366f1', 1: '#f59e0b', 2: '#22c55e', 3: '#ef4444' }

  const chartData = data ? data.map(item => ({
    name: statusMap[item.status] || '未知',
    value: item.count,
    itemStyle: { color: colorMap[item.status] || '#94a3b8' }
  })) : [
    { value: 1048, name: '已完成', itemStyle: { color: '#22c55e' } },
    { value: 735, name: '配送中', itemStyle: { color: '#f59e0b' } },
    { value: 580, name: '待接单', itemStyle: { color: '#6366f1' } },
    { value: 484, name: '已取消', itemStyle: { color: '#ef4444' } }
  ]

  const option = {
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e2e8f0',
      borderWidth: 1,
      textStyle: { color: '#1e293b' },
      padding: [12, 16],
      formatter: '{b}: {c} ({d}%)',
      extraCssText: 'box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); border-radius: 8px;'
    },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center',
      textStyle: { color: '#64748b', fontSize: 12 },
      itemGap: 16,
      itemWidth: 12,
      itemHeight: 12
    },
    series: [{
      name: '订单状态',
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold',
          color: '#1e293b'
        },
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.2)'
        }
      },
      labelLine: { show: false },
      data: chartData
    }]
  }
  pieChart.setOption(option)
}

const loadTrendData = async () => {
  try {
    const res = await getOrderDailyStatistics(trendDays.value)
    initLineChart(res.data)
  } catch (error) {
    console.error(error)
    initLineChart(null)
  }
}

const loadData = async () => {
  try {
    const [homeRes, statusRes] = await Promise.all([
      getHomeStatistics(),
      getOrderStatusStatistics()
    ])

    statistics.value = homeRes.data
    initPieChart(statusRes.data)
    loadTrendData()
  } catch (error) {
    console.error(error)
    initPieChart(null)
    initLineChart(null)
  }
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', () => {
    lineChart?.resize()
    pieChart?.resize()
  })
})

onUnmounted(() => {
  lineChart?.dispose()
  pieChart?.dispose()
})
</script>

<style scoped>
.dashboard {
  padding: 32px;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  min-height: calc(100vh - 60px);
}

/* 统计卡片 */
.stat-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08), 0 1px 2px rgba(0, 0, 0, 0.04);
  border-left: 4px solid;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255,255,255,0.4) 0%, rgba(255,255,255,0) 100%);
  pointer-events: none;
}

.stat-card:hover {
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  transform: translateY(-4px);
}

.card-primary {
  border-left-color: #4f46e5;
}
.card-success {
  border-left-color: #22c55e;
}
.card-warning {
  border-left-color: #f59e0b;
}
.card-info {
  border-left-color: #6366f1;
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  position: relative;
  z-index: 1;
}

.stat-label {
  font-size: 14px;
  color: #64748b;
  margin-bottom: 8px;
  font-weight: 500;
  letter-spacing: 0.025em;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 8px;
  line-height: 1;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
}

.stat-trend {
  font-size: 13px;
  color: #94a3b8;
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: 500;
}

.stat-trend.up {
  color: #22c55e;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  transition: transform 0.3s ease;
}

.stat-card:hover .stat-icon {
  transform: scale(1.1) rotate(5deg);
}

.card-primary .stat-icon {
  background: linear-gradient(135deg, rgba(79, 70, 229, 0.15) 0%, rgba(79, 70, 229, 0.05) 100%);
  color: #4f46e5;
}

.card-success .stat-icon {
  background: linear-gradient(135deg, rgba(34, 197, 94, 0.15) 0%, rgba(34, 197, 94, 0.05) 100%);
  color: #22c55e;
}

.card-warning .stat-icon {
  background: linear-gradient(135deg, rgba(245, 158, 11, 0.15) 0%, rgba(245, 158, 11, 0.05) 100%);
  color: #f59e0b;
}

.card-info .stat-icon {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.15) 0%, rgba(99, 102, 241, 0.05) 100%);
  color: #6366f1;
}

/* 图表区域 */
.charts-row {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.chart-card {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 28px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08), 0 1px 2px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.5);
  transition: box-shadow 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.08), 0 10px 10px -5px rgba(0, 0, 0, 0.02);
}

/* 图表头部 - 左右布局 */
.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  letter-spacing: -0.025em;
  margin: 0;
}

.chart-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 20px;
  letter-spacing: -0.025em;
}

/* 下拉选择器样式 */
.trend-select {
  flex-shrink: 0;
  margin-left: 16px;
  width: 120px;
}

.trend-select :deep(.el-input__wrapper) {
  background: #f8fafc;
  border-radius: 8px;
  box-shadow: none !important;
  border: 1px solid #e2e8f0;
  min-width: 100px;
}

.trend-select :deep(.el-input__inner) {
  color: #475569;
  font-weight: 500;
}

.chart {
  height: 320px;
}

/* 响应式 */
@media (max-width: 1280px) {
  .stat-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  .charts-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .dashboard {
    padding: 16px;
  }
  .stat-cards {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  .stat-card {
    padding: 20px;
  }
  .chart-card {
    padding: 20px;
  }
  .stat-value {
    font-size: 28px;
  }
  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  .trend-select {
    margin-left: 0;
  }
}
</style>