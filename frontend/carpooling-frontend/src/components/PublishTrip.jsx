import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { MapPin, Calendar, Clock, Euro, Users, Car, Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { useToast } from '@/components/ui/use-toast';

const PublishTrip = () => {
    const { toast } = useToast();
    const [tripData, setTripData] = useState({
        from: '',
        to: '',
        date: '',
        time: '',
        price: '',
        seats: '1',
        car: '',
        preferences: []
    });

    const handlePublish = () => {
        if (!tripData.from || !tripData.to || !tripData.date || !tripData.time || !tripData.price) {
            toast({
                title: "Champs manquants",
                description: "Veuillez remplir tous les champs obligatoires",
                variant: "destructive"
            });
            return;
        }

        const trips = JSON.parse(localStorage.getItem('publishedTrips') || '[]');
        const myTrips = JSON.parse(localStorage.getItem('myPublishedTrips') || '[]');

        const newTrip = {
            ...tripData,
            id: Date.now().toString(),
            driver: {
                name: 'Vous',
                rating: 4.9,
                trips: 12
            },
            preferences: tripData.preferences.length > 0 ? tripData.preferences : ['Non-fumeur']
        };

        trips.push(newTrip);
        myTrips.push(newTrip);

        localStorage.setItem('publishedTrips', JSON.stringify(trips));
        localStorage.setItem('myPublishedTrips', JSON.stringify(myTrips));

        toast({
            title: "Trajet publié ! 🚀",
            description: "Votre trajet est maintenant visible par tous les utilisateurs"
        });

        setTripData({
            from: '',
            to: '',
            date: '',
            time: '',
            price: '',
            seats: '1',
            car: '',
            preferences: []
        });
    };

    return (
        <section className="container mx-auto px-4 py-16">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className="max-w-3xl mx-auto"
            >
                <h2 className="text-4xl font-bold mb-8 gradient-text">
                    Publier un trajet
                </h2>

                <div className="glass-effect rounded-3xl p-8 shadow-2xl space-y-6">
                    <div className="grid md:grid-cols-2 gap-6">
                        <div className="space-y-2">
                            <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                                <MapPin className="w-4 h-4 text-blue-600" />
                                Ville de départ *
                            </label>
                            <Input
                                placeholder="Paris"
                                value={tripData.from}
                                onChange={(e) => setTripData({ ...tripData, from: e.target.value })}
                                className="bg-white/50"
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                                <MapPin className="w-4 h-4 text-purple-600" />
                                Ville d'arrivée *
                            </label>
                            <Input
                                placeholder="Lyon"
                                value={tripData.to}
                                onChange={(e) => setTripData({ ...tripData, to: e.target.value })}
                                className="bg-white/50"
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                                <Calendar className="w-4 h-4 text-pink-600" />
                                Date *
                            </label>
                            <Input
                                type="date"
                                value={tripData.date}
                                onChange={(e) => setTripData({ ...tripData, date: e.target.value })}
                                className="bg-white/50"
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                                <Clock className="w-4 h-4 text-orange-600" />
                                Heure *
                            </label>
                            <Input
                                type="time"
                                value={tripData.time}
                                onChange={(e) => setTripData({ ...tripData, time: e.target.value })}
                                className="bg-white/50"
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                                <Euro className="w-4 h-4 text-green-600" />
                                Prix par passager *
                            </label>
                            <Input
                                type="number"
                                placeholder="25"
                                value={tripData.price}
                                onChange={(e) => setTripData({ ...tripData, price: e.target.value })}
                                className="bg-white/50"
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                                <Users className="w-4 h-4 text-blue-600" />
                                Places disponibles *
                            </label>
                            <Input
                                type="number"
                                min="1"
                                max="8"
                                value={tripData.seats}
                                onChange={(e) => setTripData({ ...tripData, seats: e.target.value })}
                                className="bg-white/50"
                            />
                        </div>

                        <div className="space-y-2 md:col-span-2">
                            <label className="text-sm font-medium text-gray-700 flex items-center gap-2">
                                <Car className="w-4 h-4 text-purple-600" />
                                Modèle de voiture
                            </label>
                            <Input
                                placeholder="Renault Clio"
                                value={tripData.car}
                                onChange={(e) => setTripData({ ...tripData, car: e.target.value })}
                                className="bg-white/50"
                            />
                        </div>
                    </div>

                    <div className="border-t border-gray-200 pt-6">
                        <h3 className="font-semibold mb-4 text-gray-700">Préférences de voyage</h3>
                        <div className="flex flex-wrap gap-3">
                            {['Non-fumeur', 'Musique OK', 'Animaux OK', 'Silence apprécié', 'Discussions bienvenues'].map((pref) => (
                                <Button
                                    key={pref}
                                    variant={tripData.preferences.includes(pref) ? 'default' : 'outline'}
                                    size="sm"
                                    onClick={() => {
                                        if (tripData.preferences.includes(pref)) {
                                            setTripData({
                                                ...tripData,
                                                preferences: tripData.preferences.filter(p => p !== pref)
                                            });
                                        } else {
                                            setTripData({
                                                ...tripData,
                                                preferences: [...tripData.preferences, pref]
                                            });
                                        }
                                    }}
                                >
                                    {pref}
                                </Button>
                            ))}
                        </div>
                    </div>

                    <motion.div
                        whileHover={{ scale: 1.02 }}
                        whileTap={{ scale: 0.98 }}
                    >
                        <Button
                            onClick={handlePublish}
                            className="w-full bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white py-6 text-lg gap-2"
                        >
                            <Plus className="w-5 h-5" />
                            Publier mon trajet
                        </Button>
                    </motion.div>
                </div>
            </motion.div>
        </section>
    );
};

export default PublishTrip;