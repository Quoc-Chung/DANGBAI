// src/pages/admin/DashboardPage.tsx
import React from 'react';
import { FaUsers, FaFileAlt, FaShoppingCart, FaDollarSign, FaArrowUp, FaArrowDown } from 'react-icons/fa';
import { AdminLayout } from '../../components/layout/AdminLayout';

export const DashboardPage: React.FC = () => {
  const stats = [
    {
      title: 'Tổng người dùng',
      value: '1,234',
      change: '+12.5%',
      isIncrease: true,
      icon: FaUsers,
      color: 'from-gray-400 to-gray-600',
      bgColor: 'bg-gray-50'
    },
    {
      title: 'Bài đăng',
      value: '3,456',
      change: '+8.2%',
      isIncrease: true,
      icon: FaFileAlt,
      color: 'from-gray-400 to-gray-600',
      bgColor: 'bg-gray-50'
    },
    {
      title: 'Đơn hàng',
      value: '567',
      change: '-3.1%',
      isIncrease: false,
      icon: FaShoppingCart,
      color: 'from-gray-400 to-gray-600',
      bgColor: 'bg-gray-50'
    },
    {
      title: 'Doanh thu',
      value: '₫125M',
      change: '+15.3%',
      isIncrease: true,
      icon: FaDollarSign,
      color: 'from-gray-400 to-gray-600',
      bgColor: 'bg-gray-50'
    }
  ];

  const recentActivities = [
    { user: 'Nguyễn Văn A', action: 'đăng bài mới', item: 'iPhone 13 Pro Max', time: '5 phút trước' },
    { user: 'Trần Thị B', action: 'mua sản phẩm', item: 'MacBook Air M1', time: '15 phút trước' },
    { user: 'Lê Văn C', action: 'bình luận', item: 'AirPods Pro', time: '30 phút trước' },
    { user: 'Phạm Thị D', action: 'thích bài viết', item: 'iPad Air 5', time: '1 giờ trước' },
    { user: 'Hoàng Văn E', action: 'đăng ký tài khoản', item: '', time: '2 giờ trước' },
  ];

  const topProducts = [
    { name: 'iPhone 13 Pro Max', sales: 45, revenue: '₫831M' },
    { name: 'MacBook Air M1', sales: 38, revenue: '₫638M' },
    { name: 'AirPods Pro', sales: 67, revenue: '₫281M' },
    { name: 'iPad Air 5', sales: 29, revenue: '₫362M' },
  ];

  return (
    <AdminLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Dashboard</h1>
          <p className="text-gray-600 mt-1">Tổng quan hệ thống Tech Market</p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {stats.map((stat, index) => (
            <div
              key={index}
              className="bg-white rounded-xl shadow-lg hover:shadow-2xl transition-all duration-300 overflow-hidden border border-gray-100 transform hover:-translate-y-1"
            >
              <div className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <div className={`p-3 ${stat.bgColor} rounded-lg`}>
                    <stat.icon className={`text-2xl bg-gradient-to-r ${stat.color} bg-clip-text text-transparent`} />
                  </div>
                  <div className={`flex items-center text-sm font-semibold ${
                    stat.isIncrease ? 'text-green-600' : 'text-red-600'
                  }`}>
                    {stat.isIncrease ? <FaArrowUp className="mr-1" /> : <FaArrowDown className="mr-1" />}
                    {stat.change}
                  </div>
                </div>
                <h3 className="text-gray-600 text-sm font-medium mb-1">{stat.title}</h3>
                <p className="text-3xl font-bold text-gray-800">{stat.value}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Charts & Tables Row */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Recent Activities */}
          <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
            <h2 className="text-xl font-bold text-gray-800 mb-4">Hoạt động gần đây</h2>
            <div className="space-y-4">
              {recentActivities.map((activity, index) => (
                <div key={index} className="flex items-start space-x-3 pb-4 border-b last:border-b-0">
                  <div className="w-10 h-10 bg-gradient-to-r from-gray-500 to-gray-600 rounded-full flex items-center justify-center text-white font-semibold flex-shrink-0">
                    {activity.user.charAt(0)}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm text-gray-800">
                      <span className="font-semibold">{activity.user}</span>
                      {' '}{activity.action}
                      {activity.item && <span className="text-gray-600"> {activity.item}</span>}
                    </p>
                    <p className="text-xs text-gray-500 mt-1">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Top Products */}
          <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
            <h2 className="text-xl font-bold text-gray-800 mb-4">Sản phẩm bán chạy</h2>
            <div className="space-y-4">
              {topProducts.map((product, index) => (
                <div key={index} className="flex items-center justify-between pb-4 border-b last:border-b-0">
                  <div className="flex items-center space-x-3">
                    <div className={`w-8 h-8 rounded-lg flex items-center justify-center font-bold ${
                      index === 0 ? 'bg-gray-100 text-gray-600' :
                      index === 1 ? 'bg-gray-100 text-gray-600' :
                      index === 2 ? 'bg-gray-100 text-gray-600' :
                      'bg-gray-100 text-gray-600'
                    }`}>
                      #{index + 1}
                    </div>
                    <div>
                      <p className="font-semibold text-gray-800">{product.name}</p>
                      <p className="text-sm text-gray-500">{product.sales} đã bán</p>
                    </div>
                  </div>
                  <p className="font-bold text-green-600">{product.revenue}</p>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
          <h2 className="text-xl font-bold text-gray-800 mb-4">Thao tác nhanh</h2>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <button className="p-6 border-2 border-dashed border-gray-300 rounded-xl hover:border-gray-500 hover:bg-gray-50 transition-all duration-300 text-center transform hover:scale-105 hover:shadow-lg">
              <FaUsers className="text-4xl text-gray-500 mx-auto mb-3" />
              <p className="text-sm font-semibold text-gray-700">Thêm User</p>
            </button>
            <button className="p-6 border-2 border-dashed border-gray-300 rounded-xl hover:border-gray-500 hover:bg-gray-50 transition-all duration-300 text-center transform hover:scale-105 hover:shadow-lg">
              <FaFileAlt className="text-4xl text-gray-500 mx-auto mb-3" />
              <p className="text-sm font-semibold text-gray-700">Duyệt bài</p>
            </button>
            <button className="p-6 border-2 border-dashed border-gray-300 rounded-xl hover:border-gray-500 hover:bg-gray-50 transition-all duration-300 text-center transform hover:scale-105 hover:shadow-lg">
              <FaShoppingCart className="text-4xl text-gray-500 mx-auto mb-3" />
              <p className="text-sm font-semibold text-gray-700">Đơn hàng</p>
            </button>
            <button className="p-6 border-2 border-dashed border-gray-300 rounded-xl hover:border-gray-500 hover:bg-gray-50 transition-all duration-300 text-center transform hover:scale-105 hover:shadow-lg">
              <FaDollarSign className="text-4xl text-gray-500 mx-auto mb-3" />
              <p className="text-sm font-semibold text-gray-700">Báo cáo</p>
            </button>
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};