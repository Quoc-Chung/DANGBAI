// src/components/layout/Header.tsx
import React, { useState, useRef, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaBell, FaUserCircle, FaSignOutAlt, FaCog, FaShoppingCart } from 'react-icons/fa';
import { useAuth } from '../../hooks/useAuth';

export const Header: React.FC = () => {
  const { user, logout } = useAuth();
  const [showDropdown, setShowDropdown] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setShowDropdown(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const getAvatarUrl = () => {
    if (user?.avatarUrl) return user.avatarUrl;
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(user?.displayName || 'User')}&background=random`;
  };

  return (
    <header className="bg-white shadow-md sticky top-0 z-50 w-full">
      <div className="w-full px-4 sm:px-4 lg:px-4">
        <div className="flex justify-between items-center h-20 max-w-full">
          {/* Logo */}
          <div className="flex items-center">
            <Link to="/" className="flex items-center space-x-1">
              <div className="w-12 h-12 bg-gradient-to-r from-gray-600 to-gray-700 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold text-2xl">TM</span>
              </div>
              <span className="text-2xl font-bold text-gray-800">Tech Market</span>
            </Link>
          </div>

          {/* Right Section */}
          <div className="flex items-center space-x-5">
            {/* Cart Icon */}
            <button className="relative p-2.5 text-gray-600 hover:text-gray-800 transition">
              <FaShoppingCart className="text-2xl" />
              <span className="absolute top-0 right-0 bg-red-500 text-white text-sm rounded-full w-6 h-6 flex items-center justify-center font-semibold">
                0
              </span>
            </button>

            {/* Notifications */}
            <button className="relative p-2.5 text-gray-600 hover:text-gray-800 transition">
              <FaBell className="text-2xl" />
              <span className="absolute top-1 right-1 w-3 h-3 bg-red-500 rounded-full"></span>
            </button>

            {/* User Profile Dropdown */}
            <div className="relative" ref={dropdownRef}>
              <button
                onClick={() => setShowDropdown(!showDropdown)}
                className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-100 transition"
              >
                <img
                  src={getAvatarUrl()}
                  alt={user?.displayName}
                  className="w-12 h-12 rounded-full object-cover border-2 border-gray-500"
                />
                <div className="hidden md:block text-left">
                  <p className="text-base font-semibold text-gray-800">{user?.displayName}</p>
                  <p className="text-sm text-gray-500">@{user?.username}</p>
                </div>
              </button>

              {/* Dropdown Menu */}
              {showDropdown && (
                <div className="absolute right-0 mt-2 w-64 bg-white rounded-lg shadow-xl border border-gray-200 py-2">
                  {/* User Info */}
                  <div className="px-4 py-3 border-b border-gray-200">
                    <p className="text-base font-semibold text-gray-800">{user?.displayName}</p>
                    <p className="text-sm text-gray-500">{user?.email}</p>
                    <div className="mt-2 flex gap-1">
                      {user?.roles.map(role => (
                        <span key={role} className="px-2.5 py-1 bg-gray-100 text-gray-800 text-sm rounded-full font-medium">
                          {role.replace('ROLE_', '')}
                        </span>
                      ))}
                    </div>
                  </div>

                  {/* Menu Items */}
                  <Link
                    to="/profile"
                    className="flex items-center px-4 py-2.5 text-base text-gray-700 hover:bg-gray-100 transition"
                    onClick={() => setShowDropdown(false)}
                  >
                    <FaUserCircle className="mr-3 text-gray-400 text-lg" />
                    Trang cá nhân
                  </Link>

                  <Link
                    to="/settings"
                    className="flex items-center px-4 py-2.5 text-base text-gray-700 hover:bg-gray-100 transition"
                    onClick={() => setShowDropdown(false)}
                  >
                    <FaCog className="mr-3 text-gray-400 text-lg" />
                    Cài đặt
                  </Link>

                  <hr className="my-2 border-gray-200" />

                  {/* Logout */}
                  <button
                    onClick={() => {
                      setShowDropdown(false);
                      logout();
                    }}
                    className="w-full flex items-center px-4 py-2.5 text-base text-red-600 hover:bg-red-50 transition"
                  >
                    <FaSignOutAlt className="mr-3 text-lg" />
                    Đăng xuất
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};