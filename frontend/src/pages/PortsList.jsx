import React, { useState, useMemo } from 'react';
import { Building2, Plus, Search, Edit, Trash2, X, Check, Filter, Compass } from 'lucide-react';
import { api } from '../services/api';

const TERMINAL_LABELS = {
  STANDARD: 'Port standard',
  PETROLIER_MOHAMMEDIA: 'Terminal Pétrolier (Mohammédia)',
  CHARBONNIER_JORF: 'Terminal Charbonnier (Jorf-Lasfar)',
  CHARBONNIER_SAFI: 'Terminal Charbonnier (Safi Atlantique)'
};

export function PortsList({ ports, onRefresh }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [terminalFilter, setTerminalFilter] = useState('ALL');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingPort, setEditingPort] = useState(null);

  // Form Fields
  const [nom, setNom] = useState('');
  const [typeTerminal, setTypeTerminal] = useState('STANDARD');
  const [saving, setSaving] = useState(false);

  // Filtered ports
  const filteredPorts = useMemo(() => {
    return ports.filter(p => {
      const matchSearch = (p.nom || '').toLowerCase().includes(searchTerm.toLowerCase());
      const matchType = terminalFilter === 'ALL' || p.typeTerminal === terminalFilter;
      return matchSearch && matchType;
    });
  }, [ports, searchTerm, terminalFilter]);

  const openAddModal = () => {
    setEditingPort(null);
    setNom('');
    setTypeTerminal('STANDARD');
    setIsModalOpen(true);
  };

  const openEditModal = (port) => {
    setEditingPort(port);
    setNom(port.nom || '');
    setTypeTerminal(port.typeTerminal || 'STANDARD');
    setIsModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);

    const payload = { nom, typeTerminal };

    if (editingPort) {
      await api.updatePort(editingPort.idPort, payload);
    } else {
      await api.createPort(payload);
    }

    setSaving(false);
    setIsModalOpen(false);
    onRefresh();
  };

  const handleDelete = async (id) => {
    if (window.confirm('Voulez-vous vraiment supprimer ce port ?')) {
      await api.deletePort(id);
      onRefresh();
    }
  };

  return (
    <div className="ports-page">
      {/* Top Header Bar */}
      <div className="page-header card">
        <div className="page-title">
          <div className="title-icon bg-ocean">
            <Building2 size={28} color="#FFFFFF" />
          </div>
          <div>
            <h2>Tous les Ports du Maroc</h2>
            <p>Infrastructure portuaire sous la tutelle de l'Agence Nationale des Ports (ANP)</p>
          </div>
        </div>
        <button className="btn btn-secondary" onClick={openAddModal}>
          <Plus size={18} /> Nouveau Port
        </button>
      </div>

      {/* Ports Cards Overview Grid */}
      <div className="ports-grid">
        {filteredPorts.map((port) => (
          <div className="port-card card" key={port.idPort}>
            <div className="port-card-header">
              <div className="port-icon-box">
                <Compass size={24} color="#0077A3" />
              </div>
              <span className="badge badge-ocean">{TERMINAL_LABELS[port.typeTerminal] || 'Port standard'}</span>
            </div>

            <h3 className="port-name">{port.nom}</h3>
            <p className="port-sub">Infrastructures certifiées aux normes ANP</p>

            <div className="port-card-footer">
              <span className="port-id">ID Port: #{port.idPort}</span>
              <div className="action-btns">
                <button className="btn btn-outline btn-sm" onClick={() => openEditModal(port)}>
                  <Edit size={14} />
                </button>
                <button className="btn btn-danger btn-sm" onClick={() => handleDelete(port.idPort)}>
                  <Trash2 size={14} />
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Search & Filter Bar */}
      <div className="filter-bar card">
        <div className="search-input-box">
          <Search size={18} color="#64748B" />
          <input
            type="text"
            placeholder="Rechercher par nom de port..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <div className="filter-group">
          <Filter size={18} color="#64748B" />
          <select value={terminalFilter} onChange={(e) => setTerminalFilter(e.target.value)} className="form-control" style={{ width: '220px' }}>
            <option value="ALL">Tous les Types de Terminal</option>
            <option value="STANDARD">Port standard</option>
            <option value="PETROLIER_MOHAMMEDIA">Terminal Pétrolier (Mohammédia)</option>
            <option value="CHARBONNIER_JORF">Terminal Charbonnier (Jorf-Lasfar)</option>
            <option value="CHARBONNIER_SAFI">Terminal Charbonnier (Safi Atlantique)</option>
          </select>
        </div>
      </div>

      {/* Modal Add/Edit */}
      {isModalOpen && (
        <div className="modal-overlay" onClick={() => setIsModalOpen(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>
                <Building2 size={20} color="#D4AF37" /> {editingPort ? 'Modifier le Port' : 'Ajouter un Nouveau Port'}
              </h3>
              <button className="modal-close" onClick={() => setIsModalOpen(false)}>
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <label>Nom du Port *</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Ex: Port de Dakhla"
                    value={nom}
                    onChange={(e) => setNom(e.target.value)}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Régime Tarifaire du Port *</label>
                  <select
                    className="form-control"
                    value={typeTerminal}
                    onChange={(e) => setTypeTerminal(e.target.value)}
                    required
                  >
                    <option value="STANDARD">Port standard (Art 2.1)</option>
                    <option value="PETROLIER_MOHAMMEDIA">Terminal Pétrolier Mohammédia (Art 2.2)</option>
                    <option value="CHARBONNIER_JORF">Terminal Charbonnier Jorf-Lasfar (Art 2.3)</option>
                    <option value="CHARBONNIER_SAFI">Terminal Charbonnier Safi Atlantique (Art 2.4)</option>
                  </select>
                  <p className="field-hint">
                    Détermine le régime de calcul appliqué par le moteur (tranches TJB ou volume géométrique).
                    Les 3 régimes spéciaux correspondent à un seul port réel chacun — ne pas les réutiliser pour un autre port.
                  </p>
                </div>
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setIsModalOpen(false)}>
                  Annuler
                </button>
                <button type="submit" className="btn btn-secondary" disabled={saving}>
                  <Check size={16} /> {saving ? 'Enregistrement...' : 'Enregistrer le Port'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <style>{`
        .field-hint {
          font-size: 0.75rem;
          color: var(--anp-text-muted);
          margin-top: 0.35rem;
          line-height: 1.4;
        }

        .ports-page {
          display: flex;
          flex-direction: column;
          gap: 1.5rem;
        }

        .ports-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
          gap: 1.5rem;
        }

        .port-card {
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          border-top: 4px solid var(--anp-ocean);
        }

        .port-card-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 1rem;
        }

        .port-icon-box {
          width: 44px;
          height: 44px;
          border-radius: var(--radius-md);
          background: rgba(0, 119, 163, 0.1);
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .port-name {
          font-family: 'Outfit', sans-serif;
          font-size: 1.25rem;
          color: var(--anp-navy);
          font-weight: 700;
          margin-bottom: 0.25rem;
        }

        .port-sub {
          font-size: 0.825rem;
          color: var(--anp-text-muted);
          margin-bottom: 1.25rem;
        }

        .port-card-footer {
          display: flex;
          align-items: center;
          justify-content: space-between;
          border-top: 1px solid var(--anp-border);
          padding-top: 0.85rem;
        }

        .port-id {
          font-size: 0.8rem;
          font-weight: 700;
          color: var(--anp-text-muted);
        }
      `}</style>
    </div>
  );
}
