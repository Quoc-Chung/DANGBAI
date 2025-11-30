// src/components/layout/Sidebar.tsx
import React from 'react';
import { NavLink } from 'react-router-dom';
import type { IconType } from 'react-icons';
import {
  FaHome,
  FaShoppingBag,
  FaHeart,
  FaUser,
  FaTachometerAlt,
  FaUsers,
  FaFileAlt,
  FaCog,
  FaChartBar
} from 'react-icons/fa';
import { useAuth } from '../../hooks/useAuth';

interface MenuItem {
  path: string;
  label: string;
  icon: IconType;
}

export const Sidebar: React.FC = () => {
  const { isAdmin } = useAuth();

  const userMenuItems: MenuItem[] = [
    { path: '/', label: 'Trang chủ', icon: FaHome },
    { path: '/products', label: 'Sản phẩm', icon: FaShoppingBag },
    { path: '/favorites', label: 'Yêu thích', icon: FaHeart },
    { path: '/profile', label: 'Tài khoản', icon: FaUser },
  ];

  const adminMenuItems: MenuItem[] = [
    { path: '/admin/dashboard', label: 'Dashboard', icon: FaTachometerAlt },
    { path: '/admin/users', label: 'Quản lý Users', icon: FaUsers },
    { path: '/admin/posts', label: 'Quản lý Posts', icon: FaFileAlt },
    { path: '/admin/analytics', label: 'Thống kê', icon: FaChartBar },
    { path: '/admin/settings', label: 'Cài đặt', icon: FaCog },
  ];

  const menuItems = isAdmin ? adminMenuItems : userMenuItems;

  return (
    <aside className="w-[296px] bg-white shadow-lg h-[calc(100vh-5rem)] sticky top-20 overflow-y-auto">
      <nav className="p-4 space-y-1">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            end={item.path === '/' || item.path === '/admin/dashboard'}
            className={({ isActive }) =>
              `flex items-center space-x-3 px-4 py-3.5 rounded-lg transition-all duration-200 ${
                isActive
                  ? 'bg-gradient-to-r from-gray-600 to-gray-700 text-white shadow-md'
                  : 'text-gray-700 hover:bg-gray-100'
              }`
            }
          >
            {({ isActive }) => (
              <>
                <item.icon className={`text-xl ${isActive ? 'text-white' : 'text-gray-500'}`} />
                <span className="font-medium text-base">{item.label}</span>
              </>
            )}
          </NavLink>
        ))}
      </nav>

      {/* Footer */}
      <div className="absolute bottom-0 w-full p-4 border-t border-gray-200 bg-gray-50">
        <p className="text-sm text-gray-500 text-center">
          © 2024 Tech Market
        </p>
      </div>
    </aside>
  );
};