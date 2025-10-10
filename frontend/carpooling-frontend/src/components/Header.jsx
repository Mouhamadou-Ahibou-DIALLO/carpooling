import React from 'react';
import { motion } from 'framer-motion';
import { Car, Search, Calendar, User } from 'lucide-react';
import { Button } from '@/components/ui/button';

const Header = ({ activeTab, setActiveTab }) => {
    return (
        <motion.header
            initial={{ y: -100 }}
            animate={{ y: 0 }}
            className="sticky top-0 z-50 glass-effect shadow-lg"
        >
            <div className="container mx-auto px-4 py-4">
                <div className="flex items-center justify-between">
                    <motion.div
                        whileHover={{ scale: 1.05 }}
                        className="flex items-center gap-2 cursor-pointer"
                        onClick={() => setActiveTab('search')}
                    >
                        <div className="bg-gradient-to-r from-blue-600 to-purple-600 p-2 rounded-xl">
                            <Car className="w-6 h-6 text-white" />
                        </div>
                        <h1 className="text-2xl font-bold gradient-text">CovoitExpress</h1>
                    </motion.div>

                    <nav className="hidden md:flex items-center gap-2">
                        <Button
                            variant={activeTab === 'search' ? 'default' : 'ghost'}
                            onClick={() => setActiveTab('search')}
                            className="gap-2"
                        >
                            <Search className="w-4 h-4" />
                            Rechercher
                        </Button>
                        <Button
                            variant={activeTab === 'publish' ? 'default' : 'ghost'}
                            onClick={() => setActiveTab('publish')}
                            className="gap-2"
                        >
                            <Car className="w-4 h-4" />
                            Publier un trajet
                        </Button>
                        <Button
                            variant={activeTab === 'mytrips' ? 'default' : 'ghost'}
                            onClick={() => setActiveTab('mytrips')}
                            className="gap-2"
                        >
                            <Calendar className="w-4 h-4" />
                            Mes trajets
                        </Button>
                    </nav>

                    <Button variant="outline" className="gap-2">
                        <User className="w-4 h-4" />
                        <span className="hidden md:inline">Mon compte</span>
                    </Button>
                </div>

                <div className="md:hidden flex gap-2 mt-4">
                    <Button
                        size="sm"
                        variant={activeTab === 'search' ? 'default' : 'ghost'}
                        onClick={() => setActiveTab('search')}
                        className="flex-1 gap-1"
                    >
                        <Search className="w-4 h-4" />
                        Rechercher
                    </Button>
                    <Button
                        size="sm"
                        variant={activeTab === 'publish' ? 'default' : 'ghost'}
                        onClick={() => setActiveTab('publish')}
                        className="flex-1 gap-1"
                    >
                        <Car className="w-4 h-4" />
                        Publier
                    </Button>
                    <Button
                        size="sm"
                        variant={activeTab === 'mytrips' ? 'default' : 'ghost'}
                        onClick={() => setActiveTab('mytrips')}
                        className="flex-1 gap-1"
                    >
                        <Calendar className="w-4 h-4" />
                        Mes trajets
                    </Button>
                </div>
            </div>
        </motion.header>
    );
};

export default Header;