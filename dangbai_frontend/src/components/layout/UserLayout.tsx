// src/components/layout/UserLayout.tsx
import React from 'react';
import { Header } from './Header';
import { Sidebar } from './Sidebar';

interface UserLayoutProps {
  children: React.ReactNode;
}

export const UserLayout: React.FC<UserLayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <div className="flex">
        <Sidebar />
        <main className="flex-1 p-6 overflow-y-auto h-[calc(100vh-5rem)]">
          {children}
        </main>
      </div>
    </div>
  );
};
