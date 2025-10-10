import React from 'react';
import { motion } from 'framer-motion';
import { Sparkles } from 'lucide-react';

const Hero = () => {
    return (
        <section className="relative overflow-hidden py-20 px-4">
            <div className="absolute inset-0 bg-gradient-to-r from-blue-600/10 via-purple-600/10 to-pink-600/10" />

            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.8 }}
                className="container mx-auto text-center relative z-10"
            >
                <motion.div
                    animate={{ rotate: 360 }}
                    transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
                    className="inline-block mb-6"
                >
                    <Sparkles className="w-12 h-12 text-purple-600" />
                </motion.div>

                <h2 className="text-4xl md:text-6xl font-bold mb-6 gradient-text">
                    Voyagez ensemble,<br />économisez chaque jour
                </h2>

                <p className="text-xl text-gray-700 max-w-2xl mx-auto mb-8">
                    Partagez vos trajets quotidiens avec des personnes de confiance.
                    Économique, écologique et convivial !
                </p>

                <div className="flex flex-wrap justify-center gap-8 mt-12">
                    <motion.div
                        whileHover={{ scale: 1.05 }}
                        className="glass-effect rounded-2xl p-6 max-w-xs"
                    >
                        <div className="text-4xl font-bold text-blue-600 mb-2">50%</div>
                        <p className="text-gray-700">d'économies en moyenne</p>
                    </motion.div>

                    <motion.div
                        whileHover={{ scale: 1.05 }}
                        className="glass-effect rounded-2xl p-6 max-w-xs"
                    >
                        <div className="text-4xl font-bold text-purple-600 mb-2">10k+</div>
                        <p className="text-gray-700">trajets partagés</p>
                    </motion.div>

                    <motion.div
                        whileHover={{ scale: 1.05 }}
                        className="glass-effect rounded-2xl p-6 max-w-xs"
                    >
                        <div className="text-4xl font-bold text-pink-600 mb-2">5★</div>
                        <p className="text-gray-700">satisfaction moyenne</p>
                    </motion.div>
                </div>
            </motion.div>
        </section>
    );
};

export default Hero;