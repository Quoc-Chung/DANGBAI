// src/components/post/FilterCard.tsx
import React, { useState } from 'react';
import { FaSearch, FaFilter, FaMapMarkerAlt, FaTag, FaSortAmountDown, FaTimes, FaChevronUp, FaChevronDown } from 'react-icons/fa';

interface FilterCardProps {
  onFilterChange?: (filters: FilterState) => void;
}

export interface FilterState {
  search: string;
  location: string;
  category: string;
  priceRange: string;
  condition: string;
  sortBy: string;
}

export const FilterCard: React.FC<FilterCardProps> = ({ onFilterChange }) => {
  const [filters, setFilters] = useState<FilterState>({
    search: '',
    location: '',
    category: '',
    priceRange: '',
    condition: '',
    sortBy: 'newest'
  });

  const [isExpanded, setIsExpanded] = useState(true);

  const handleFilterChange = (key: keyof FilterState, value: string) => {
    const newFilters = { ...filters, [key]: value };
    setFilters(newFilters);
    onFilterChange?.(newFilters);
  };

  const clearFilters = () => {
    const clearedFilters = {
      search: '',
      location: '',
      category: '',
      priceRange: '',
      condition: '',
      sortBy: 'newest'
    };
    setFilters(clearedFilters);
    onFilterChange?.(clearedFilters);
  };

  const hasActiveFilters = filters.search || filters.location || filters.category || filters.priceRange || filters.condition || filters.sortBy !== 'newest';

  return (
    <div className="bg-white rounded-xl shadow-xl border border-gray-200 overflow-hidden">
      {/* Header with Gradient */}
      <div className="bg-gradient-to-r from-gray-600 via-gray-500 to-gray-600 p-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 bg-white/20 backdrop-blur-sm rounded-lg flex items-center justify-center">
              <FaFilter className="text-white text-lg" />
            </div>
            <div>
              <h3 className="text-lg font-bold text-white">Lọc & Tìm kiếm</h3>
              <p className="text-xs text-white/80">Tìm bài đăng phù hợp</p>
            </div>
          </div>
          <button
            onClick={() => setIsExpanded(!isExpanded)}
            className="p-2 hover:bg-white/20 rounded-lg transition text-white"
            title={isExpanded ? 'Thu gọn' : 'Mở rộng'}
          >
            {isExpanded ? <FaChevronUp /> : <FaChevronDown />}
          </button>
        </div>
      </div>

      <div className="p-5">
        {/* Search */}
        <div className="mb-5">
          <label className="block text-sm font-semibold text-gray-700 mb-2">
            Tìm kiếm
          </label>
          <div className="relative">
            <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
              <FaSearch className="text-gray-400" />
            </div>
            <input
              type="text"
              value={filters.search}
              onChange={(e) => handleFilterChange('search', e.target.value)}
              placeholder="Nhập từ khóa..."
              className="w-full pl-12 pr-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition bg-gray-50 hover:bg-white"
            />
          </div>
        </div>

        {/* Expanded Filters */}
        {isExpanded && (
          <div className="space-y-5 animate-in fade-in duration-300">
            {/* Location */}
            <div>
              <label className="flex items-center text-sm font-semibold text-gray-700 mb-2">
                <FaMapMarkerAlt className="mr-2 text-gray-600" />
                Vị trí
              </label>
              <select
                value={filters.location}
                onChange={(e) => handleFilterChange('location', e.target.value)}
                className="w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition bg-gray-50 hover:bg-white cursor-pointer"
              >
                <option value="">Tất cả vị trí</option>
                <option value="Hà Nội">Hà Nội</option>
                <option value="TP.HCM">TP.HCM</option>
                <option value="Đà Nẵng">Đà Nẵng</option>
                <option value="Hải Phòng">Hải Phòng</option>
                <option value="Cần Thơ">Cần Thơ</option>
              </select>
            </div>

            {/* Category */}
            <div>
              <label className="flex items-center text-sm font-semibold text-gray-700 mb-2">
                <FaTag className="mr-2 text-gray-600" />
                Danh mục
              </label>
              <select
                value={filters.category}
                onChange={(e) => handleFilterChange('category', e.target.value)}
                className="w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition bg-gray-50 hover:bg-white cursor-pointer"
              >
                <option value="">Tất cả danh mục</option>
                <option value="Laptop">Laptop</option>
                <option value="Điện thoại">Điện thoại</option>
                <option value="Tablet">Tablet</option>
                <option value="Phụ kiện">Phụ kiện</option>
                <option value="Khác">Khác</option>
              </select>
            </div>

            {/* Price Range */}
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                💰 Khoảng giá
              </label>
              <select
                value={filters.priceRange}
                onChange={(e) => handleFilterChange('priceRange', e.target.value)}
                className="w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition bg-gray-50 hover:bg-white cursor-pointer"
              >
                <option value="">Tất cả mức giá</option>
                <option value="0-5000000">Dưới 5 triệu</option>
                <option value="5000000-10000000">5 - 10 triệu</option>
                <option value="10000000-20000000">10 - 20 triệu</option>
                <option value="20000000-50000000">20 - 50 triệu</option>
                <option value="50000000-999999999">Trên 50 triệu</option>
              </select>
            </div>

            {/* Condition */}
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                ✨ Tình trạng
              </label>
              <select
                value={filters.condition}
                onChange={(e) => handleFilterChange('condition', e.target.value)}
                className="w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition bg-gray-50 hover:bg-white cursor-pointer"
              >
                <option value="">Tất cả tình trạng</option>
                <option value="99%">99% - Như mới</option>
                <option value="95%">95% - Rất tốt</option>
                <option value="90%">90% - Tốt</option>
                <option value="85%">85% - Khá</option>
              </select>
            </div>

            {/* Sort By */}
            <div>
              <label className="flex items-center text-sm font-semibold text-gray-700 mb-2">
                <FaSortAmountDown className="mr-2 text-gray-600" />
                Sắp xếp
              </label>
              <select
                value={filters.sortBy}
                onChange={(e) => handleFilterChange('sortBy', e.target.value)}
                className="w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition bg-gray-50 hover:bg-white cursor-pointer"
              >
                <option value="newest">Mới nhất</option>
                <option value="oldest">Cũ nhất</option>
                <option value="price-low">Giá: Thấp → Cao</option>
                <option value="price-high">Giá: Cao → Thấp</option>
                <option value="most-reactions">Nhiều tương tác nhất</option>
              </select>
            </div>
          </div>
        )}

        {/* Clear Filters Button */}
        {hasActiveFilters && (
          <button
            onClick={clearFilters}
            className="w-full mt-5 px-4 py-3 bg-gradient-to-r from-gray-500 to-gray-600 hover:from-gray-600 hover:to-gray-700 text-white rounded-xl font-semibold transition-all duration-300 transform hover:scale-[1.02] shadow-md hover:shadow-lg flex items-center justify-center space-x-2"
          >
            <FaTimes className="text-sm" />
            <span>Xóa bộ lọc</span>
          </button>
        )}
      </div>
    </div>
  );
};
