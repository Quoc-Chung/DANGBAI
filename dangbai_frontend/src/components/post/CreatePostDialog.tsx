// src/components/post/CreatePostDialog.tsx
import React, { useState, useEffect, useRef } from 'react';
import { FaTimes, FaImage, FaMapMarkerAlt, FaTag, FaDollarSign, FaSpinner } from 'react-icons/fa';
import { postApi, type CreatePostRequest, type Category } from '../../api/post.api';
import { toast } from 'react-toastify';

interface CreatePostDialogProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess?: () => void;
}

export const CreatePostDialog: React.FC<CreatePostDialogProps> = ({
  isOpen,
  onClose,
  onSuccess,
}) => {
  const [formData, setFormData] = useState<CreatePostRequest>({
    title: '',
    description: '',
    price: 0,
    location: '',
    categoryId: 0,
  });
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState<Partial<Record<keyof CreatePostRequest | 'images', string>>>({});
  const fileInputRef = useRef<HTMLInputElement>(null);

  // Load categories on mount
  useEffect(() => {
    const loadCategories = async () => {
      try {
        const cats = await postApi.getCategories();
        setCategories(cats);
        if (cats.length > 0 && formData.categoryId === 0) {
          setFormData(prev => ({ ...prev, categoryId: cats[0].id }));
        }
      } catch (error) {
        console.error('Failed to load categories:', error);
      }
    };
    if (isOpen) {
      loadCategories();
    }
  }, [isOpen]);

  // Reset form when dialog closes
  useEffect(() => {
    if (!isOpen) {
      setFormData({
        title: '',
        description: '',
        price: 0,
        location: '',
        categoryId: 0,
      });
      setImages([]);
      setImagePreviews([]);
      setErrors({});
    }
  }, [isOpen]);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'price' || name === 'categoryId' ? Number(value) : value,
    }));
    if (errors[name as keyof typeof errors]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || []);
    
    if (files.length === 0) return;

    // Validate file types and sizes
    const validFiles = files.filter(file => {
      if (!file.type.startsWith('image/')) {
        toast.error(`${file.name} không phải là file ảnh`);
        return false;
      }
      if (file.size > 5 * 1024 * 1024) {
        toast.error(`${file.name} vượt quá 5MB`);
        return false;
      }
      return true;
    });

    if (validFiles.length === 0) return;

    const newImages = [...images, ...validFiles];
    setImages(newImages);

    // Create previews
    const newPreviews = validFiles.map(file => URL.createObjectURL(file));
    setImagePreviews(prev => [...prev, ...newPreviews]);
  };

  const removeImage = (index: number) => {
    const newImages = images.filter((_, i) => i !== index);
    const newPreviews = imagePreviews.filter((_, i) => i !== index);
    
    // Revoke object URL to free memory
    URL.revokeObjectURL(imagePreviews[index]);
    
    setImages(newImages);
    setImagePreviews(newPreviews);
  };

  const validate = (): boolean => {
    const newErrors: typeof errors = {};

    if (!formData.title.trim()) {
      newErrors.title = 'Tiêu đề không được để trống';
    }

    if (!formData.description.trim()) {
      newErrors.description = 'Mô tả không được để trống';
    }

    if (formData.price <= 0) {
      newErrors.price = 'Giá phải lớn hơn 0';
    }

    if (!formData.location.trim()) {
      newErrors.location = 'Vị trí không được để trống';
    }

    if (formData.categoryId === 0) {
      newErrors.categoryId = 'Vui lòng chọn danh mục';
    }

    if (images.length === 0) {
      newErrors.images = 'Vui lòng thêm ít nhất một ảnh';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validate()) return;

    setIsLoading(true);
    try {
      await postApi.createPost(formData, images);
      toast.success('Tạo bài đăng thành công!');
      onSuccess?.();
      onClose();
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi tạo bài đăng';
      toast.error(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  const formatPrice = (value: number): string => {
    return new Intl.NumberFormat('vi-VN').format(value);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-3xl max-h-[90vh] overflow-hidden flex flex-col">
        {/* Header */}
        <div className="bg-gradient-to-r from-gray-600 via-gray-500 to-gray-600 p-6 flex items-center justify-between">
          <div>
            <h2 className="text-2xl font-bold text-white">Tạo bài đăng mới</h2>
            <p className="text-white/80 text-sm mt-1">Chia sẻ sản phẩm của bạn với cộng đồng</p>
          </div>
          <button
            onClick={onClose}
            className="w-10 h-10 bg-white/20 hover:bg-white/30 rounded-full flex items-center justify-center transition text-white"
          >
            <FaTimes />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto p-6">
          <div className="space-y-5">
            {/* Title */}
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                Tiêu đề <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                name="title"
                value={formData.title}
                onChange={handleInputChange}
                className={`w-full px-4 py-3 border-2 ${
                  errors.title ? 'border-red-500' : 'border-gray-200'
                } rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition`}
                placeholder="Ví dụ: Bán iPhone 14 Pro Max 256GB"
              />
              {errors.title && (
                <p className="mt-1 text-sm text-red-500">{errors.title}</p>
              )}
            </div>

            {/* Description */}
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                Mô tả <span className="text-red-500">*</span>
              </label>
              <textarea
                name="description"
                value={formData.description}
                onChange={handleInputChange}
                rows={4}
                className={`w-full px-4 py-3 border-2 ${
                  errors.description ? 'border-red-500' : 'border-gray-200'
                } rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition resize-none`}
                placeholder="Mô tả chi tiết về sản phẩm của bạn..."
              />
              {errors.description && (
                <p className="mt-1 text-sm text-red-500">{errors.description}</p>
              )}
            </div>

            {/* Price and Location Row */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
              {/* Price */}
              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-2">
                  <FaDollarSign className="inline mr-1" />
                  Giá (VNĐ) <span className="text-red-500">*</span>
                </label>
                <input
                  type="number"
                  name="price"
                  value={formData.price || ''}
                  onChange={handleInputChange}
                  min="0"
                  className={`w-full px-4 py-3 border-2 ${
                    errors.price ? 'border-red-500' : 'border-gray-200'
                  } rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition`}
                  placeholder="15000000"
                />
                {formData.price > 0 && (
                  <p className="mt-1 text-sm text-gray-600">
                    {formatPrice(formData.price)} VNĐ
                  </p>
                )}
                {errors.price && (
                  <p className="mt-1 text-sm text-red-500">{errors.price}</p>
                )}
              </div>

              {/* Location */}
              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-2">
                  <FaMapMarkerAlt className="inline mr-1" />
                  Vị trí <span className="text-red-500">*</span>
                </label>
                <select
                  name="location"
                  value={formData.location}
                  onChange={handleInputChange}
                  className={`w-full px-4 py-3 border-2 ${
                    errors.location ? 'border-red-500' : 'border-gray-200'
                  } rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition cursor-pointer`}
                >
                  <option value="">Chọn vị trí</option>
                  <option value="Hà Nội">Hà Nội</option>
                  <option value="TP.HCM">TP.HCM</option>
                  <option value="Đà Nẵng">Đà Nẵng</option>
                  <option value="Hải Phòng">Hải Phòng</option>
                  <option value="Cần Thơ">Cần Thơ</option>
                  <option value="Khác">Khác</option>
                </select>
                {errors.location && (
                  <p className="mt-1 text-sm text-red-500">{errors.location}</p>
                )}
              </div>
            </div>

            {/* Category */}
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                <FaTag className="inline mr-1" />
                Danh mục <span className="text-red-500">*</span>
              </label>
              <select
                name="categoryId"
                value={formData.categoryId}
                onChange={handleInputChange}
                className={`w-full px-4 py-3 border-2 ${
                  errors.categoryId ? 'border-red-500' : 'border-gray-200'
                } rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition cursor-pointer`}
              >
                <option value="0">Chọn danh mục</option>
                {categories.map(cat => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name}
                  </option>
                ))}
              </select>
              {errors.categoryId && (
                <p className="mt-1 text-sm text-red-500">{errors.categoryId}</p>
              )}
            </div>

            {/* Images */}
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                <FaImage className="inline mr-1" />
                Hình ảnh <span className="text-red-500">*</span>
              </label>
              <input
                ref={fileInputRef}
                type="file"
                accept="image/*"
                multiple
                onChange={handleImageChange}
                className="hidden"
              />
              <button
                type="button"
                onClick={() => fileInputRef.current?.click()}
                className="w-full px-4 py-3 border-2 border-dashed border-gray-300 rounded-xl hover:border-blue-500 hover:bg-blue-50 transition text-gray-600 flex items-center justify-center space-x-2"
              >
                <FaImage />
                <span>Chọn ảnh (tối đa 5MB/ảnh)</span>
              </button>
              {errors.images && (
                <p className="mt-1 text-sm text-red-500">{errors.images}</p>
              )}

              {/* Image Previews */}
              {imagePreviews.length > 0 && (
                <div className="mt-4 grid grid-cols-2 md:grid-cols-3 gap-4">
                  {imagePreviews.map((preview, index) => (
                    <div key={index} className="relative group">
                      <img
                        src={preview}
                        alt={`Preview ${index + 1}`}
                        className="w-full h-32 object-cover rounded-lg"
                      />
                      <button
                        type="button"
                        onClick={() => removeImage(index)}
                        className="absolute top-2 right-2 w-8 h-8 bg-red-500 hover:bg-red-600 text-white rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition"
                      >
                        <FaTimes className="text-xs" />
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </form>

        {/* Footer */}
        <div className="border-t border-gray-200 p-6 flex items-center justify-end space-x-3">
          <button
            type="button"
            onClick={onClose}
            disabled={isLoading}
            className="px-6 py-3 border-2 border-gray-300 text-gray-700 rounded-xl font-semibold hover:bg-gray-50 transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Hủy
          </button>
          <button
            type="submit"
            onClick={handleSubmit}
            disabled={isLoading}
            className="px-6 py-3 bg-gradient-to-r from-gray-600 to-gray-700 hover:from-gray-700 hover:to-gray-800 text-white rounded-xl font-semibold transition-all duration-300 transform hover:scale-[1.02] shadow-lg hover:shadow-xl disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none flex items-center space-x-2"
          >
            {isLoading ? (
              <>
                <FaSpinner className="animate-spin" />
                <span>Đang tạo...</span>
              </>
            ) : (
              <span>Tạo bài đăng</span>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};

