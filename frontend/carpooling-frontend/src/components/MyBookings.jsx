import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { MapPin, Calendar, Clock, Euro, Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useToast } from '@/components/ui/use-toast';

const MyBookings = () => {
    const { toast } = useToast();
    const [bookings, setBookings] = useState([]);

    useEffect(() => {
        const savedBookings = localStorage.getItem('myBookings');
        if (savedBookings) {
            setBookings(JSON.parse(savedBookings));
        }
    }, []);

    const handleCancel = (bookingId) => {
        const updatedBookings = bookings.filter(b => b.bookingId !== bookingId);
        setBookings(updatedBookings);
        localStorage.setItem('myBookings', JSON.stringify(updatedBookings));

        toast({
            title: "Réservation annulée",
            description: "Votre réservation a été annulée avec succès"
        });
    };

    return (
        <div className="space-y-4">
            {bookings.length === 0 ? (
                <div className="glass-effect rounded-2xl p-12 text-center">
                    <p className="text-gray-500 text-lg">Vous n'avez aucune réservation</p>
                </div>
            ) : (
                bookings.map((booking, index) => (
                    <motion.div
                        key={booking.bookingId}
                        initial={{ opacity: 0, x: -20 }}
                        animate={{ opacity: 1, x: 0 }}
                        transition={{ delay: index * 0.1 }}
                        className="glass-effect rounded-2xl p-6 shadow-lg"
                    >
                        <div className="flex items-start justify-between">
                            <div className="flex-1">
                                <div className="flex items-center gap-3 mb-4">
                                    <div className="flex items-center gap-2">
                                        <MapPin className="w-5 h-5 text-blue-600" />
                                        <span className="font-semibold text-lg">{booking.from}</span>
                                    </div>
                                    <span className="text-gray-400">→</span>
                                    <div className="flex items-center gap-2">
                                        <MapPin className="w-5 h-5 text-purple-600" />
                                        <span className="font-semibold text-lg">{booking.to}</span>
                                    </div>
                                </div>

                                <div className="grid md:grid-cols-3 gap-4 text-gray-700">
                                    <div className="flex items-center gap-2">
                                        <Calendar className="w-4 h-4" />
                                        <span>{booking.date}</span>
                                    </div>
                                    <div className="flex items-center gap-2">
                                        <Clock className="w-4 h-4" />
                                        <span>{booking.time}</span>
                                    </div>
                                    <div className="flex items-center gap-2">
                                        <Euro className="w-4 h-4" />
                                        <span className="font-semibold">{booking.price}€</span>
                                    </div>
                                </div>

                                <div className="mt-4 p-3 bg-green-50 rounded-lg">
                                    <span className="text-green-700 font-medium">✓ Réservation confirmée</span>
                                </div>
                            </div>

                            <Button
                                variant="destructive"
                                size="icon"
                                onClick={() => handleCancel(booking.bookingId)}
                                className="ml-4"
                            >
                                <Trash2 className="w-4 h-4" />
                            </Button>
                        </div>
                    </motion.div>
                ))
            )}
        </div>
    );
};

export default MyBookings;