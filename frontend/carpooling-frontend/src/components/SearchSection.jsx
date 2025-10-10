import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { MapPin, Calendar, Search, Users } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { useToast } from '@/components/ui/use-toast';

const SearchSection = () => {
    const { toast } = useToast();
    const [searchData, setSearchData] = useState({
        from: '',
        to: '',
        date: '',
        passengers: '1'
    });

    const handleSearch = () => {
        if (!searchData.from || !searchData.to || !searchData.date) {
            toast({
                title: "Champs manquants",
                description: "Veuillez remplir tous les champs de recherche",
                variant: "destructive"
            });
            return;
        }

        toast({
            title: "Recherche en cours...",
            description: `Recherche de trajets de ${searchData.from} à ${searchData.to}`
        });
    };

    return (
        <section className="container mx-auto px-4 -mt-8 relative z-20">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3 }}
                className="glass-effect rounded-3xl p-8 shadow-2xl"
            >
                <div className="grid md:grid-cols-4 gap-4">
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                            <MapPin className="w-4 h-4 text-blue-600" />
                            Départ
                        </label>
                        <Input
                            placeholder="Paris"
                            value={searchData.from}
                            onChange={(e) => setSearchData({ ...searchData, from: e.target.value })}
                            className="bg-white/50"
                        />
                    </div>

                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                            <MapPin className="w-4 h-4 text-purple-600" />
                            Arrivée
                        </label>
                        <Input
                            placeholder="Lyon"
                            value={searchData.to}
                            onChange={(e) => setSearchData({ ...searchData, to: e.target.value })}
                            className="bg-white/50"
                        />
                    </div>

                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                            <Calendar className="w-4 h-4 text-pink-600" />
                            Date
                        </label>
                        <Input
                            type="date"
                            value={searchData.date}
                            onChange={(e) => setSearchData({ ...searchData, date: e.target.value })}
                            className="bg-white/50"
                        />
                    </div>

                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                            <Users className="w-4 h-4 text-green-600" />
                            Passagers
                        </label>
                        <Input
                            type="number"
                            min="1"
                            max="8"
                            value={searchData.passengers}
                            onChange={(e) => setSearchData({ ...searchData, passengers: e.target.value })}
                            className="bg-white/50"
                        />
                    </div>
                </div>

                <motion.div
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                    className="mt-6"
                >
                    <Button
                        onClick={handleSearch}
                        className="w-full bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white py-6 text-lg gap-2"
                    >
                        <Search className="w-5 h-5" />
                        Rechercher un trajet
                    </Button>
                </motion.div>
            </motion.div>
        </section>
    );
};

export default SearchSection;